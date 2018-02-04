package hu.helixlab.tracking.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "kml_files", schema = "public")
public class KmlFiles extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5396362420193848654L;
	private int kmlFileId;
	private String filename;
	private byte[] file;

	public KmlFiles() {
	}

	public KmlFiles(int kmlFileId, String filename, byte[] file) {
		this.kmlFileId = kmlFileId;
		this.filename = filename;
		this.file = file;
	}

	@Id
	@SequenceGenerator(name="kmlSequence", sequenceName="kml_files_kml_file_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="kmlSequence")
	@Column(name = "kml_file_id", unique = true, nullable = false)
	public int getKmlFileId() {
		return this.kmlFileId;
	}

	public void setKmlFileId(int kmlFileId) {
		this.kmlFileId = kmlFileId;
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
		return "KmlFiles [kmlFileId=" + kmlFileId + ", filename=" + filename + ", file=" + Arrays.toString(file) + "]";
	}
	
	
}
