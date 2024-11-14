const fs = require('fs');
const path = require('path');
const TABLE_DATA = require('./data.json');

// overkill
const README_FILE = path.resolve(__dirname, '../README.md')
const README_HEADER_FILE = path.resolve(__dirname, './readme_header.md');
const TABLE_HEADER_FILE = path.resolve(__dirname, './table_header.md');

const README_HEADER = fs.readFileSync(README_HEADER_FILE).toString();
const TABLE_HEADER = fs.readFileSync(TABLE_HEADER_FILE).toString();

// building table for README.json
// header

// RUN ========================================
const tableContent = TABLE_DATA.map(e => `| ${e.id} | ${e.date} | ${e.name} | ${e.cat} | ${e.description} |\n`);
const writeData = `${README_HEADER}\n${TABLE_HEADER}\n${tableContent}`;

console.log("README: generate readme from data.json");
fs.writeFileSync(README_FILE, writeData);
console.log("README: Generated");
// END ========================================