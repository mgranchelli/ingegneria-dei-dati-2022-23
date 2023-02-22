package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize
@JsonSerialize
public class Row {
	
	@JsonProperty("LinkBlue")
    private String LinkBlue;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Href")
    private String Href;
    
	public String getLinkBlue() {
		return LinkBlue;
	}
	public void setLinkBlue(String linkBlue) {
		LinkBlue = linkBlue;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHref() {
		return Href;
	}
	public void setHref(String href) {
		Href = href;
	}

    
}
