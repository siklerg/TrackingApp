package hu.helixlab.tracking.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tracking_parameters", schema = "public")
public class TrackingParameters extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1214459359521616250L;
	private int trackingParameterId;
	private Tracking tracking;
	private double length;
	private Integer level;
	private String description;

	public TrackingParameters() {
	}

	
	public TrackingParameters(int trackingParameterId, Tracking tracking, double length, Integer level,
			String description) {
		this.trackingParameterId = trackingParameterId;
		this.tracking = tracking;
		this.length = length;
		this.level = level;
		this.description = description;
	}

	@Id
	@SequenceGenerator(name="tracking_parameters_tracking_parameter_id_seq", sequenceName="tracking_parameters_tracking_parameter_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="tracking_parameters_tracking_parameter_id_seq")


	@Column(name = "tracking_parameter_id", unique = true, nullable = false)
	public int getTrackingParameterId() {
		return this.trackingParameterId;
	}

	public void setTrackingParameterId(int trackingParameterId) {
		this.trackingParameterId = trackingParameterId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tracking_id")
	public Tracking getTracking() {
		return this.tracking;
	}

	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}

	@Column(name = "length", nullable = false, precision = 17, scale = 17)
	public double getLength() {
		return this.length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	@Column(name = "level")
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "description", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Hossz: " + length + ", Szint: " + level +
				", Leírás: " + description;
	}
}
