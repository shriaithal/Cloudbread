package edu.sjsu.cloudbread.response;

import java.util.Date;
import java.util.List;

import edu.sjsu.cloudbread.dto.FoodDetailsDTO;
import edu.sjsu.cloudbread.model.User;
import edu.sjsu.cloudbread.model.FoodDetails.CategoryEnum;

public class FoodDetailsResponse extends GenericResponse {

	private static final long serialVersionUID = 1L;
	List<FoodDetailsDTO> foodDetailsDTOList;

	public FoodDetailsResponse() {
	}

	public List<FoodDetailsDTO> getFoodDetailsDTOList() {
		return foodDetailsDTOList;
	}

	public void setFoodDetailsDTOList(List<FoodDetailsDTO> foodDetailsDTOList) {
		this.foodDetailsDTOList = foodDetailsDTOList;
	}

}
