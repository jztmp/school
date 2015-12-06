package cn.com.bettle.net.entity;

// default package

/**
 * OfUser entity. @author MyEclipse Persistence Tools
 */

public class OfUser implements java.io.Serializable {

	// Fields

	private String username;
	private String plainPassword;
	private String encryptedPassword;
	private String name;
	private String email;
	private String creationDate;
	private String modificationDate;

	// Constructors

	/** default constructor */
	public OfUser() {
	}

	/** minimal constructor */
	public OfUser(String username, String creationDate, String modificationDate) {
		this.username = username;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	/** full constructor */
	public OfUser(String username, String plainPassword, String encryptedPassword, String name, String email,
			String creationDate, String modificationDate) {
		this.username = username;
		this.plainPassword = plainPassword;
		this.encryptedPassword = encryptedPassword;
		this.name = name;
		this.email = email;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	// Property accessors

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlainPassword() {
		return this.plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getEncryptedPassword() {
		return this.encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}

}