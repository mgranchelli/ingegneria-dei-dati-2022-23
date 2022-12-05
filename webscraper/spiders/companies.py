import scrapy
import re
from scrapy.exceptions import CloseSpider
import unicodedata


class CompaniesMarketCapSpider(scrapy.Spider):
    name = 'market_cap'
    start_urls = ['https://companiesmarketcap.com/']

    def parse(self, response):
        
        for company in response.xpath('//div[contains(@class,"table-container")]/table/tbody/tr'):
            yield response.follow(company.xpath('td[2]/div[2]/a/@href').get(), callback=self.parse_item_details)

        link_next_page = response.xpath('//nav/ul/li/a[last()]/@href').get()
        if link_next_page is not None:
            yield response.follow(link_next_page, callback=self.parse)

        

    def parse_item_details(self, response):
        change_year = response.xpath('//div[@class="col-lg-6"]/div[2]/div[3]/div[1]/span/text()').get()
        if change_year != None:
            change_year = change_year.strip()
        yield {
            'name': response.xpath("//div[@class='company-title-container']/div[1]/text()").get().strip(),
            'code': response.xpath("//div[@class='company-title-container']/div[2]/text()").get().strip(),
            'rank': response.xpath('//div[@class="col-lg-6"]/div[1]/div[1]/div[1]/text()').get().replace('#', '').strip(),
            'marketcap': response.xpath('//div[@class="col-lg-6"]/div[1]/div[2]/div[1]/text()').get().strip(),
            'country': response.xpath('//div[@class="line1"]//span[@class="responsive-hidden"]/text()').get().strip(),
            'sharePrice': response.xpath('//div[@class="col-lg-6"]/div[2]/div[1]/div[1]/text()').get().strip(),
            'change(1day)': response.xpath('//div[@class="col-lg-6"]/div[2]/div[2]/div[1]/span/text()').get().strip(),
            'change(1year)': change_year,
            'categories': [re.sub('\W+','', x) for x in response.xpath('//div[@class="info-box categories-box"]/div[1]/a/text()').getall()],
        }


class ValueTodaySpider(scrapy.Spider):
    name = 'value_today'
    start_urls = ['https://www.value.today/']
    def parse(self, response):
        
        for company in response.xpath('//li[@class="row well clearfix"]/div'):

            ceo = company.xpath('div[3]/div/div[contains(., "CEO:")]/../div[2]/div/a/text()').get()
            if ceo != None:
                ceo = ceo.strip()
            
            marketvalue_2022 = company.xpath('div/div/div[contains(., "Market Value (Jan-07-2022)")]/../div[2]/text()').get()
            if marketvalue_2022 != None:
                marketvalue_2022 = marketvalue_2022.strip()

            marketvalue_2020 = company.xpath('div/div/div[contains(., "Market Value (Jan 1st 2020)")]/../div[2]/text()').get()
            if marketvalue_2020 != None:
                marketvalue_2020 = marketvalue_2020.strip()
            
            world_rank_2020 = company.xpath('div/div/div[contains(., "World Rank (Jan-2020)")]/../div[2]/text()').get()
            if world_rank_2020 != None:
                world_rank_2020 = world_rank_2020.strip()

            headquarters_country = company.xpath('div[2]/div[contains(., "Headquarters Country")]/div[2]/div/a/text()').get()
            if headquarters_country != None:
                headquarters_country = headquarters_country.strip()

            employees = company.xpath('div[2]/div[contains(., "Number of Employees")]/div[2]/text()').get()
            if employees != None:
                employees = employees.strip()
            
            annual_revenue = company.xpath('div[4]/div/div[contains(., "Annual Revenue in USD")]/../div[2]/text()').get()
            if annual_revenue != None:
                annual_revenue = annual_revenue.strip()

            annual_net_income = company.xpath('div[4]/div/div[contains(., "Annual Net Income in USD")]/../div[2]/text()').get()
            if annual_net_income != None:
                annual_net_income = annual_net_income.strip()
            
            yield {
                'name': company.xpath('div/div/h2/a/text()').get().strip(),
                'ceo': ceo,
                'market value (Jan-07-2022)': marketvalue_2022,
                'world rank (Jan-07-2022)': company.xpath('div/div[2]/div[2]/text()').get().strip(),
                'market value (Jan 1st 2020)': marketvalue_2020,
                'world rank (Jan-2020)': world_rank_2020,
                'headquarters country': headquarters_country,
                'number of employees': employees,
                'company business': company.xpath('div[2]/div[contains(., "Company Business")]/div[2]/div/a/text()').getall(),
                'annual revenue in USD': annual_revenue,
                'annual net income in USD': annual_net_income,
                'company website': company.xpath('div[3]/div[contains(., "Company Website:")]/div/a/@href').get(),
            }
        
        link_next_page = response.xpath('//li[@class="pager__item pager__item--next"]/a/@href').get()
        if link_next_page is not None:
            yield response.follow(link_next_page, callback=self.parse)


class Disfold(scrapy.Spider):
    name = 'disfold'
    start_urls = ['https://disfold.com/world/companies/']

    def parse(self, response):
        
        for company in response.xpath('//div[@class="card-content"]/table/tbody/tr'):
            rank = company.xpath('td/text()').get().strip()
            code = company.xpath('td[4]/a/text()').get().strip()

            country = company.xpath('td[5]/a/text()[2]').get()
            if country != None:
                country = country.strip()
            else:
                country = company.xpath('td[5]/text()').get().strip()
            
            sector = company.xpath('td[6]/a/text()[2]').get()
            if sector != None:
                sector = sector.strip()
            else:
                sector = company.xpath('td[6]/text()').get().strip()

            industry = company.xpath('td[7]/a/text()[2]').get()
            if industry != None:
                industry = industry.strip()
            else:
                industry = company.xpath('td[7]/text()').get().strip()

            yield response.follow(company.xpath('td[2]/a/@href').get(), callback=self.parse_item_details, 
                    meta={'rank': rank, 'code': code, 'country': country, 'sector': sector, 'industry': industry})

        link_next_page = response.xpath('//li[@class="waves-effect"][last()-1]/a/@href').get()
        if link_next_page is not None:
            yield response.follow(link_next_page, callback=self.parse)

        

    def parse_item_details(self, response):

        ceo = response.xpath("//p[contains(., 'CEO:')]/text()").get()
        if ceo != None:
            ceo = re.sub(' +', ' ', ceo.replace('CEO: ', '').strip())
        
        headquarters = response.xpath("//p[contains(., 'Headquarters:')]/text()").get()
        if headquarters != None:
            headquarters = re.sub('\s+', ' ', headquarters.replace("Headquarters: ", "").strip())
        
        employees = response.xpath("//p[contains(., 'Employees:')]/text()").get()
        if employees != None:
            employees = employees.replace('Employees: ', '').strip()

        founded = response.xpath("//p[contains(., 'Founded:')]/text()").get()
        if founded != None:
            founded = unicodedata.normalize('NFKC', founded.replace('&nbsp;', ' ').replace('Founded: ', '').strip())

        marketcap = response.xpath('//p[@class="mcap"]/../p[@class="green-text text-lighten-4"]/text()').get()
        if marketcap == None:
            marketcap = response.xpath('//p[@class="mcap"]/text()').get().strip()
        else:
            marketcap= marketcap.strip()
            
        revenue = response.xpath("//strong[contains(., 'Revenue:')]/../text()").get()
        if revenue != None:
            revenue = unicodedata.normalize('NFKC', revenue.strip())

        net_income = response.xpath("//strong[contains(., 'Net income:')]/../text()").get()
        if net_income != None:
            net_income = unicodedata.normalize('NFKC', net_income.strip())

        yield {
            'name': response.xpath("//p[contains(., 'Company')]/../h1/text()").get().strip(),
            'code': response.meta.get('code'),
            'rank': response.meta.get('rank'),
            'ceo': ceo,
            'country': response.meta.get('country'),
            'sector': response.meta.get('sector'),
            'industry': response.meta.get('industry'),
            'headquarters': headquarters,
            'employees': employees,
            'founded': founded,
            'marketcap': marketcap,
            'revenue': revenue,
            'net income': net_income,
            'link': response.xpath('//div[@class="card-content company-details"]/a/@href').getall(),
        }
