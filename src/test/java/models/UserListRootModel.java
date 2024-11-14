package models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListRootModel{

	@JsonProperty("per_page")
	private int perPage;

	private int total;

	private List<DataItem> data;

	private int page;

	@JsonProperty("total_pages")
	private int totalPages;

	private Support support;
}