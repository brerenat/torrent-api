package brere.nat.torrent.api.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TorrentResult {

	private String title = null;
	private String category = null;
	private String download = null;
	private int seeders = 0;
	private int leachers = 0;
	private long size = 0L;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
	private Date pubdate = null;
	@JsonProperty("episode_info")
	private EpisodeInfo episodeInfo = null;
	private int ranked = 1;
	@JsonProperty("info_page")
	private String infoPage = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public int getSeeders() {
		return seeders;
	}

	public void setSeeders(int seeders) {
		this.seeders = seeders;
	}

	public int getLeachers() {
		return leachers;
	}

	public void setLeachers(int leachers) {
		this.leachers = leachers;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getPubdate() {
		return pubdate;
	}

	public void setPubdate(Date pubdate) {
		this.pubdate = pubdate;
	}

	public EpisodeInfo getEpisodeInfo() {
		return episodeInfo;
	}

	public void setEpisodeInfo(EpisodeInfo episodeInfo) {
		this.episodeInfo = episodeInfo;
	}

	public int getRanked() {
		return ranked;
	}

	public void setRanked(int ranked) {
		this.ranked = ranked;
	}

	public String getInfoPage() {
		return infoPage;
	}

	public void setInfoPage(String infoPage) {
		this.infoPage = infoPage;
	}
}
