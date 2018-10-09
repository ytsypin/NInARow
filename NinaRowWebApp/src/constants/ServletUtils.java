package constants;

import general.GameManager;
import general.UserManager;

import javax.servlet.ServletContext;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
	private static final String GAME_MANAGER_ATTRIBUTE_NAME = "gameManager";
	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained unchronicled for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object gameManagerLock = new Object();
	private static final Object chatManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

    public static GameManager getGameManager(ServletContext servletContext) {
		synchronized (gameManagerLock) {
			if (servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(GAME_MANAGER_ATTRIBUTE_NAME, new GameManager());
			}
		}
		return (GameManager) servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME);

	}
}
