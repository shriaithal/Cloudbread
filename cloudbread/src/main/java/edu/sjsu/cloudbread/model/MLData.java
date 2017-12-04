package edu.sjsu.cloudbread.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Entity
@Table(name = "mldata")
public class MLData {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String day;
	private String weather;
	private String predicted;
	private Timestamp createdTime;
	private Timestamp actualTime;
	private BigDecimal ratings;
	private BigDecimal distance;
	private BigDecimal occupancy;
	private Integer capacity;
	private String foodType;
	private Integer goodReviews;
	private Integer badReviews;
	private BigDecimal priceRating;
	private BigDecimal amountCooked;
	private BigDecimal wastage;
	

	public MLData() {
	}

	public MLData(Integer id, String name, BigDecimal ratings, BigDecimal distance, String day, BigDecimal occupancy,
			Integer capacity, String weather, String foodType, Integer goodReviews, Integer badReviews,
			BigDecimal priceRating, BigDecimal amountCooked, BigDecimal wastage, String predicted, Timestamp createdTime,
			Timestamp actualTime) {
		super();
		this.id = id;
		this.name = name;
		this.ratings = ratings;
		this.distance = distance;
		this.day = day;
		this.occupancy = occupancy;
		this.capacity = capacity;
		this.weather = weather;
		this.foodType = foodType;
		this.goodReviews = goodReviews;
		this.badReviews = badReviews;
		this.priceRating = priceRating;
		this.amountCooked = amountCooked;
		this.wastage = wastage;
		this.predicted = predicted;
		this.createdTime = createdTime;
		this.actualTime = actualTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRatings() {
		return ratings;
	}

	public void setRatings(BigDecimal ratings) {
		this.ratings = ratings;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public BigDecimal getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(BigDecimal occupancy) {
		this.occupancy = occupancy;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public Integer getGoodReviews() {
		return goodReviews;
	}

	public void setGoodReviews(Integer goodReviews) {
		this.goodReviews = goodReviews;
	}

	public Integer getBadReviews() {
		return badReviews;
	}

	public void setBadReviews(Integer badReviews) {
		this.badReviews = badReviews;
	}

	public BigDecimal getPriceRating() {
		return priceRating;
	}

	public void setPriceRating(BigDecimal priceRating) {
		this.priceRating = priceRating;
	}

	public BigDecimal getAmountCooked() {
		return amountCooked;
	}

	public void setAmountCooked(BigDecimal amountCooked) {
		this.amountCooked = amountCooked;
	}

	public BigDecimal getWastage() {
		return wastage;
	}

	public void setWastage(BigDecimal wastage) {
		this.wastage = wastage;
	}

	public String getPredicted() {
		return predicted;
	}

	public void setPredicted(String predicted) {
		this.predicted = predicted;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getActualTime() {
		return actualTime;
	}

	public void setActualTime(Timestamp actualTime) {
		this.actualTime = actualTime;
	}

	@Override
	public String toString() {
		return "MLData [id=" + id + ", name=" + name + ", ratings=" + ratings + ", distance=" + distance + ", day="
				+ day + ", occupancy=" + occupancy + ", capacity=" + capacity + ", weather=" + weather + ", foodType="
				+ foodType + ", goodReviews=" + goodReviews + ", badReviews=" + badReviews + ", priceRating="
				+ priceRating + ", amountCooked=" + amountCooked + ", wastage=" + wastage + ", predicted=" + predicted
				+ ", createdTime=" + createdTime + ", actualTime=" + actualTime + "]";
	}

}
