import requests
from bs4 import BeautifulSoup

# read from id file
id_list = []
with open("prop_id.txt", "r") as f:
    for l in f.readlines():
        id_list.append(int(l))
    f.close();

data = []

# read from url
WEBROOT = "https://www.domain.com.au/"
print(f"scraping {len(id_list)} shortlisted props")
## repeat this for all ids
for i in range(0,len(id_list)):
    URL = f"{WEBROOT}{id_list[i]}"
    # URL = "https://www.domain.com.au/18-flemington-street-st-johns-park-nsw-2176-2017564192"
    page = requests.get(URL)
    soup = BeautifulSoup(page.content, 'html.parser')
    d = {}

    listingSummary = soup.find("div", {"data-testid" : "listing-details__summary-left-column"})
    smolSummaries = listingSummary.find_all("span", {"data-testid": "property-features-text-container"})

    d_bed = smolSummaries[0].get_text().split()[0]     ## 1 bed
    d_bath = smolSummaries[1].get_text().split()[0]    ## 2 bath
    d_parking = smolSummaries[2].get_text().split()[0] ## 3 parking
    d_sqm = "-" 
    if len(smolSummaries) == 4:                         ## 4 sqm
        d_sqm = smolSummaries[3].get_text().split()[0]

    ## address 
    d_address = soup.find("img", class_="css-bh4wo8").get("alt").split(",")
    d_road = d_address[0].strip()
    d_sub = d_address[1][:-9].strip()

    ## property type 
    d_type =  listingSummary.find("div", {"data-testid": "listing-summary-property-type"}).get_text()
    d_sale_type = listingSummary.find("div", {"data-testid": "listing-details__summary-title"}).get_text()
    d_auc_date = "-"
    d_auc_time = "-"
    if "auction" in d_sale_type.lower(): #listing-details__auction-times
        try:
            d_auc = soup.find("div", {"data-testid": "listing-details__auction-times"}).find("div", class_="css-o2iu3z").find_all("span")
            d_auc_date = d_auc[0].get_text()
            d_auc_time = d_auc[1].get_text()
        except:
            print(f"auction date error at {URL}")

    d = {
        "url": URL,
        "sale_type": d_sale_type,
        "auc_date": d_auc_date,
        "auc_time": d_auc_time,
        "road": d_road,
        "suburb": d_sub,
        "bed": d_bed,
        "bath": d_bath,
        "parking": d_parking,
        "sqm": d_sqm,
        "type": d_type,
    }
    data.append(d)
    print(f"done {i+1} of {len(id_list)}")
## end repeat

# read the data

# --------------------------------------------------------------
## CSV format
## ID ## Address ## Suburb ## Bed ## Bath ## Parking ## SQRM ## Type

CSV_FILE = "output.csv"
output = open(CSV_FILE, "w")

CSV_HEADERS = [
    "url", "sale_type", "auc_date", "auc_time","road", "suburb", "bed", "bath", "parking", "sqm", "type", 
    ]

header = ""
for h in range(0, len(CSV_HEADERS)-1):  
    header = f"{header}{CSV_HEADERS[h]}~"
header = f"{header}{CSV_HEADERS[-1]}\n"
output.write(header)

for dd in data:
    output.write(f"{dd['url']}~{dd['sale_type']}~{dd['auc_date']}~{dd['auc_time']}~{dd['road']}~{dd['suburb']}~{dd['bed']}~{dd['bath']}~{dd['parking']}~{dd['sqm']}~{dd['type']}\n")
## save to csv  