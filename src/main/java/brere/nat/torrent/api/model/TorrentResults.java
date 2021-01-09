package brere.nat.torrent.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TorrentResults {
	
	@JsonProperty("torrent_results")
	private List<TorrentResult> torrentResults = null;

	public List<TorrentResult> getTorrentResults() {
		return torrentResults;
	}

	public void setTorrentResults(List<TorrentResult> torrentResults) {
		this.torrentResults = torrentResults;
	}
}
