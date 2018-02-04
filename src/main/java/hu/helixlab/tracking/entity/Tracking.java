package hu.helixlab.tracking.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "tracking", schema = "public")
public class Tracking extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2794542720926505107L;
	private int trackingId;
	private KmlFiles kmlFiles;
	private Users users;
	private Set<PicFiles> picFileses = new HashSet<PicFiles>(0);
	private Set<TrackingParameters> trackingParameterses = new HashSet<TrackingParameters>(0);
	private Set<TrackingRegions> trackingRegionses = new HashSet<TrackingRegions>(0);

	public Tracking() {
	}

	

	public Tracking(int trackingId, KmlFiles kmlFiles, Users users) {
		this.trackingId = trackingId;
		this.kmlFiles = kmlFiles;
		this.users = users;
		}
		

	@Id
	@SequenceGenerator(name="tracking_tracking_id_seq", sequenceName="tracking_tracking_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="tracking_tracking_id_seq")


	@Column(name = "tracking_id", unique = true, nullable = false)
	public int getTrackingId() {
		return this.trackingId;
	}

	public void setTrackingId(int trackingId) {
		this.trackingId = trackingId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "kml_file_id")
	public KmlFiles getKmlFiles() {
		return this.kmlFiles;
	}

	public void setKmlFiles(KmlFiles kmlFiles) {
		this.kmlFiles = kmlFiles;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "username")
	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "tracking")
	public Set<PicFiles> getPicFileses() {
		return this.picFileses;
	}

	public void setPicFileses(Set<PicFiles> picFileses) {
		this.picFileses = picFileses;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "tracking")
	public Set<TrackingParameters> getTrackingParameterses() {
		return this.trackingParameterses;
	}

	public void setTrackingParameterses(Set<TrackingParameters> trackingParameterses) {
		this.trackingParameterses = trackingParameterses;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "tracking")
	public Set<TrackingRegions> getTrackingRegionses() {
		return this.trackingRegionses;
	}

	public void setTrackingRegionses(Set<TrackingRegions> trackingRegionses) {
		this.trackingRegionses = trackingRegionses;
	}

	@Override
	public String toString() {
		return "Tracking [trackingId=" + trackingId + ", kmlFiles=" + kmlFiles + ", users=" + users + "]";
	}

	

}
