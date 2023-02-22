# Web scraper for extracting information about companies
Python project that uses the [scrapy framework](https://scrapy.org) to scrape informations from websites.

This project is the homework 5 of 2022-2023 Ingegneria dei Dati course at Roma Tre University. The webscraper allows to scrape information about companies from 3 websites: [CompaniesMarketCap](https://companiesmarketcap.com/), [Value.Today](https://www.value.today/) and [Disfold](https://disfold.com/world/companies/).

## Getting Started 
```bash
$ git clone https://github.com/mgranchelli/companies-scraper.git
$ pip install -r requirements.txt
```

## Running the Spider
The project has spiders named as `market_cap`, `value_today` and `disfold` which can be executed using following commands:
```bash
$ scrapy crawl <spider name>
```
To store the output to a JSON/CSV file:
```bash
$ scrapy crawl <name of spider> -o <output file name>.json
$ scrapy crawl <name of spider> -o <output file name>.csv
```