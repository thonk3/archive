const fs = require('fs');
const tableData = require('./data.json');

const readmeHeader = fs.readFileSync('./assets/readme_header.md').toString();

// building table
// header
const tableHeader = `
| id | date | name | description |
|:--:|:--:|:--|:--|
`
let tableContent = "";
tableData.forEach(e => tableContent += `| ${e.id} | ${e.date} | ${e.name} | ${e.description} |\n`);

const writeData = readmeHeader + tableHeader + tableContent

fs.writeFileSync('./README.md', writeData);
// content
console.log(readmeHeader);
console.log(tableHeader);

