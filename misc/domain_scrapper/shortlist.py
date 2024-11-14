
from bs4 import BeautifulSoup

dir = "./shortlist_raw/"
pages = ["one.html", "two.html", "three.html"]
total = 0

## write list to file
output = open("prop_id.txt", "w")

# loop this thing
for page in pages:  
    soup = BeautifulSoup(open(dir + page, encoding="utf8"), "html.parser");
    elems = soup.find_all("div", class_="css-eztut6");

    print(f"page {page} count: {len(elems)}")
    total = total + len(elems)

    ## Write to file
    for e in elems:
        id = e.get("data-listing-id")
        output.write(f"{id}\n")


print(f"total in list: {total}")