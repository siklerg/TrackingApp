package hu.helixlab.tracking.entity;


import java.io.Serializable;
import java.util.Arrays;

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
@Table(name = "pic_files", schema = "public")
public class PicFiles extends BaseEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7930516157033370341L;
	private int picFileId;
	private Tracking tracking;
	private String filename;
	private byte[] file;

	public PicFiles() {
	}

	
	

	public PicFiles(int picFileId, Tracking tracking, String filename, byte[] file) {
		this.picFileId = picFileId;
		this.tracking = tracking;
		this.filename = filename;
		this.file = file;
	}

	@Id
	@SequenceGenerator(name="pic_files_pic_file_id_seq", sequenceName="pic_files_pic_file_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="pic_files_pic_file_id_seq")
	@Column(name = "pic_file_id", unique = true, nullable = false)
	public int getPicFileId() {
		return this.picFileId;
	}

	public void setPicFileId(int picFileId) {
		this.picFileId = picFileId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tracking_id")
	public Tracking getTracking() {
		return this.tracking;
	}

	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}

	@Column(name = "filename", nullable = false, length = 50)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "file", nullable = false)
	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "PicFiles [picFileId=" + picFileId + ", tracking=" + tracking + ", filename=" + filename + ", file="
				+ Arrays.toString(file) + "]";
	}
	

}
