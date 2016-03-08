package sk.tomsik68.mclauncher.api.login;

import java.util.List;

/**
 * Session is what is obtained in login process.
 *
 * @author Tomsik68
 */
public interface ISession {
	/** A simple class for user properties. A property is a name-value pair. */
	public final class Prop {
		public String name, value;
	}

	/**
	 * @return User properties tied with this session
	 */
	public List<Prop> getProperties();

	/**
	 * @return ID of the session
	 */
	public String getSessionID();

	/**
	 * @return Type of this session
	 */
	public ESessionType getType();

	/**
	 * @return Username of this session
	 */
	public String getUsername();

	/**
	 * @return Player's UUID
	 */
	public String getUUID();

}
