package hu.helixlab.tracking.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "regions", schema = "public")
public class Regions extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1331489039502037525L;
	private int regionId;
	private String regionName;
	private Set<TrackingRegions> trackingRegionses = new HashSet<TrackingRegions>(0);
	

	public Regions() {
	}

	public Regions(int regionId, String regionName) {
		this.regionId = regionId;
		this.regionName = regionName;
	}



	@Id
	@SequenceGenerator(name="regions_region_id_seq", sequenceName="regions_region_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="regions_region_id_seq")

	@Column(name = "region_id", unique = true, nullable = false)
	public int getRegionId() {
		return this.regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	@Column(name = "region_name", nullable = false, length = 200)
	public String getRegionName() {
		return this.regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "regions")
	public Set<TrackingRegions> getTrackingRegionses() {
		return this.trackingRegionses;
	}

	public void setTrackingRegionses(Set<TrackingRegions> trackingRegionses) {
		this.trackingRegionses = trackingRegionses;
	}

	@Override
	public String toString() {
		return "Regions [regionId=" + regionId + ", regionName=" + regionName + "]";
	}
}
