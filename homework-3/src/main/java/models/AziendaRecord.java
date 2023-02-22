package models;

public class AziendaRecord {

	private String id;
	private String name;
	private String country;
	private String sector;
	private String founded;
	private String marketcap;
	private String revenue;
	private String employees;
	private String links;
	private String ceo;
	private String idDocDB;

	public AziendaRecord(String id, String name, String country, String sector, String founded, String marketcap,
			String revenue, String employees, String links, String ceo) {
		this.setId(id);
		this.name = name;
		this.country = country;
		this.sector = sector;
		this.founded = founded;
		this.marketcap = marketcap;
		this.revenue = revenue;
		this.employees = employees;
		this.links = links;
		this.ceo = ceo;
		this.idDocDB = null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		if (country == null) {
			return "";
		}
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSector() {
		if (sector == null) {
			return "";
		}
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getFounded() {
		if (founded == null) {
			return "";
		}
		return founded;
	}

	public void setFounded(String founded) {
		this.founded = founded;
	}

	public String getMarketcap() {
		if (marketcap == null) {
			return "";
		}
		return marketcap;
	}

	public void setMarketcap(String marketcap) {
		this.marketcap = marketcap;
	}

	public String getRevenue() {
		if (revenue == null) {
			return "";
		}
		return revenue;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getEmployees() {
		if (employees == null) {
			return "";
		}
		return employees;
	}

	public void setEmployees(String employees) {
		this.employees = employees;
	}

	public String getLinks() {
		if (links == null) {
			return "";
		}
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getCeo() {
		if (ceo == null) {
			return "";
		}
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getIdDocDB() {
		if (idDocDB == null) {
			return "";
		}
		return idDocDB;
	}

	public void setIdDocDB(Integer idDocDB) {
		if (idDocDB == null) {
			this.idDocDB =  "";
		}
		else {
			this.idDocDB = idDocDB.toString();
		}
		
	}

	@Override
	public String toString() {
		return "Azienda{" + "name='" + this.name + '\'' + 
				", country='" + this.country + '\'' + 
				", sector='" + this.sector + '\'' + 
				", founded='" + this.founded + '\'' + 
				", marketCap='" + this.marketcap + '\'' + 
				", revenue='" + this.revenue + '\'' + 
				", employees='" + this.employees + '\'' + 
				", links='" + this.links + '\'' + 
				", ceo='" + this.ceo + '\'' + 
				", idLinkDB='" + this.idDocDB + '\'' 
				+ '}';
	}

}
