package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Support{

	@JsonProperty("text")
	private String text;

	@JsonProperty("url")
	private String url;
}