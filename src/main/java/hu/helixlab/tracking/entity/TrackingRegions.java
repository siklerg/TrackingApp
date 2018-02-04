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
@Table(name = "tracking_regions", schema = "public")
public class TrackingRegions extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4401585807743846346L;

	private int trackingRegionId;
	private Regions regions;
	private Tracking tracking;

	public TrackingRegions() {
	}

	

	public TrackingRegions(int trackingRegionId, Regions regions, Tracking tracking) {
		this.trackingRegionId = trackingRegionId;
		this.regions = regions;
		this.tracking = tracking;
	}

	@Id
	@SequenceGenerator(name="tracking_regions_tracking_region_id_seq", sequenceName="tracking_regions_tracking_region_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="tracking_regions_tracking_region_id_seq")

	@Column(name = "tracking_region_id", unique = true, nullable = false)
	public int getTrackingRegionId() {
		return this.trackingRegionId;
	}

	public void setTrackingRegionId(int trackingRegionId) {
		this.trackingRegionId = trackingRegionId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "region_id")
	public Regions getRegions() {
		return this.regions;
	}

	public void setRegions(Regions regions) {
		this.regions = regions;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tracking_id")
	public Tracking getTracking() {
		return this.tracking;
	}

	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}

	@Override
	public String toString() {
		return "TrackingRegions [trackingRegionId=" + trackingRegionId + ", regions=" + regions + ", tracking="
				+ tracking + "]";
	}
	
	

}
