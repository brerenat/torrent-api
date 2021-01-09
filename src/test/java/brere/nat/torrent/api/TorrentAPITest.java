package brere.nat.torrent.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brere.nat.torrent.api.TorrentAPI;
import brere.nat.torrent.api.model.EpisodeInfo;
import brere.nat.torrent.api.model.TorrentResult;
import brere.nat.torrent.api.model.utils.TorrentAPIException;

class TorrentAPITest {
	
	private static final Logger LOG = LoggerFactory.getLogger(TorrentAPITest.class);
	private static final String VIKINGS = "tt2306299";
	private static TorrentAPI torrentAPI;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		torrentAPI = new TorrentAPI("Ginger Plex", 20);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		torrentAPI = null;
	}

	@Test
	void getTorrentByIMDBID() {
		try {
			for (final TorrentResult tr : torrentAPI.getTorrentByIMDBID(VIKINGS)) {
				EpisodeInfo ep = tr.getEpisodeInfo();
				LOG.info("Title :" + tr.getTitle());
				LOG.info("S" + ep.getSeasonnum()+"E"+ep.getEpnum());
				LOG.info("Cat :" + tr.getCategory());
				LOG.info("getDownload :" + tr.getDownload());
				LOG.info("getInfoPage :" + tr.getInfoPage());
				LOG.info("getLeachers :" + tr.getLeachers());
				LOG.info("getRanked :" + tr.getRanked());
				LOG.info("getSeeders :" + tr.getSeeders());
				LOG.info("getSize :" + tr.getSize());
				LOG.info("getPubdate :" + tr.getPubdate());
				
				LOG.info("getAirdate :" + ep.getAirdate());
				LOG.info("getEpnum :" + ep.getEpnum());
				LOG.info("getImdb :" + ep.getImdb());
				LOG.info("getSeasonnum :" + ep.getSeasonnum());
				LOG.info("getThemoviedb :" + ep.getThemoviedb());
				LOG.info("getTitle :" + ep.getTitle());
				LOG.info("getTvdb :" + ep.getTvdb());
				LOG.info("getTvrage :" + ep.getTvrage());
			}
		} catch (TorrentAPIException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

}
