package savingFile;

public class TempSaving {

	private static boolean ffmpeg = false;
	private static String videoURL;
	private static boolean alreadySaved = false;
	private static String savingPath;
	
	public static void withFfmpeg(boolean used) {
		ffmpeg = used;
	}
	
	public static void setURL(String path) {
		videoURL = path;
	}

	public static boolean isFfmpeg() {
		return ffmpeg;
	}

	public static String getVideoURL() {
		return videoURL;
	}

	public static boolean isAlreadySaved() {
		return alreadySaved;
	}

	public static void setAlreadySaved(boolean alreadySaved) {
		TempSaving.alreadySaved = alreadySaved;
	}

	public static String getSavingPath() {
		return savingPath;
	}

	public static void setSavingPath(String savingPath) {
		TempSaving.savingPath = savingPath;
	}
	
	
	
}
