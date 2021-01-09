package brere.nat.torrent.api.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeInfo {
	
	private String imdb = null;
	private String tvrage = null;
	private String tvdb = null;
	private String themoviedb = null;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date airdate = null;
	private int epnum = 0;
	private int seasonnum = 0;
	private String title = null;

	public String getImdb() {
		return imdb;
	}

	public void setImdb(String imdb) {
		this.imdb = imdb;
	}

	public String getTvrage() {
		return tvrage;
	}

	public void setTvrage(String tvrage) {
		this.tvrage = tvrage;
	}

	public String getTvdb() {
		return tvdb;
	}

	public void setTvdb(String tvdb) {
		this.tvdb = tvdb;
	}

	public String getThemoviedb() {
		return themoviedb;
	}

	public void setThemoviedb(String themoviedb) {
		this.themoviedb = themoviedb;
	}

	public Date getAirdate() {
		return airdate;
	}

	public void setAirdate(Date airdate) {
		this.airdate = airdate;
	}

	public int getEpnum() {
		return epnum;
	}

	public void setEpnum(int epnum) {
		this.epnum = epnum;
	}

	public int getSeasonnum() {
		return seasonnum;
	}

	public void setSeasonnum(int seasonnum) {
		this.seasonnum = seasonnum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
