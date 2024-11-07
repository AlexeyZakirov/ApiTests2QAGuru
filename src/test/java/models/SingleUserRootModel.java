package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleUserRootModel{

	@JsonProperty("data")
	private DataItem data;

	@JsonProperty("support")
	private Support support;
}