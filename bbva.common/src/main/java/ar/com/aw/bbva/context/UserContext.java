package ar.com.aw.bbva.context;

import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import io.jsonwebtoken.lang.Strings;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class UserContext {

	private String				userName;
	private String				token;
	private String				bpmToken;
	private String				accessToken;
	private Date				expiration;
	private String				userGroupsBase64Deflated;

	private String				userModulesInsPermissionBase64Deflated;
	private String				userModulesUpdPermissionBase64Deflated;
	private String				userModulesDelPermissionBase64Deflated;
	private String				userModulesVerPermissionBase64Deflated;

	private Collection<String>	userGroups;
	private Collection<String>	userModulesInsPermission;
	private Collection<String>	userModulesUpdPermission;
	private Collection<String>	userModulesDelPermission;
	private Collection<String>	userModulesVerPermission;

	public UserContext(String userName, String token, String bpmToken, String accessToken, Date expiration,
			String userGroupsBase64Deflated,
			String userModulesInsPermissionBase64Deflated,
			String userModulesUpdPermissionBase64Deflated,
			String userModulesDelPermissionBase64Deflated,
			String userModulesVerPermissionBase64Deflated) {
		super();
		this.userName = userName;
		this.token = token;
		this.accessToken = accessToken;
		this.bpmToken = bpmToken;
		this.expiration = expiration;
		this.userGroupsBase64Deflated = userGroupsBase64Deflated;
		this.userModulesInsPermissionBase64Deflated = userModulesInsPermissionBase64Deflated;
		this.userModulesUpdPermissionBase64Deflated = userModulesUpdPermissionBase64Deflated;
		this.userModulesDelPermissionBase64Deflated = userModulesDelPermissionBase64Deflated;
		this.userModulesVerPermissionBase64Deflated = userModulesVerPermissionBase64Deflated;

	}

	public String getUserName() {
		return userName;
	}

	public String getToken() {
		return token;
	}

	public String getBpmToken() {
		return bpmToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public boolean hasExpired() {
		return new Date().after(expiration);
	}

	public boolean almostExpires() {
		Long expirationDifference = expiration.getTime() - new Date().getTime();
		return expirationDifference > 0 && expirationDifference <= 60000;
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getUserGroups() {
		if (userGroupsBase64Deflated == null || userGroupsBase64Deflated.trim().isEmpty())
			return Collections.EMPTY_LIST;

		return userGroups != null ? userGroups : (userGroups = Strings.commaDelimitedListToSet(
				new String(CompressionCodecs.DEFLATE.decompress(TextCodec.BASE64URL.decode(userGroupsBase64Deflated)),
						Strings.UTF_8)));
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getUserModulesInsPermission() {
		if (userModulesInsPermissionBase64Deflated == null || userModulesInsPermissionBase64Deflated.trim().isEmpty())
			return Collections.EMPTY_LIST;

		return userModulesInsPermission != null ? userModulesInsPermission
				: (userModulesInsPermission = Strings.commaDelimitedListToSet(
						new String(CompressionCodecs.DEFLATE
								.decompress(TextCodec.BASE64URL.decode(userModulesInsPermissionBase64Deflated)),
								Strings.UTF_8)));
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getUserModulesUpdPermission() {
		if (userModulesUpdPermissionBase64Deflated == null || userModulesUpdPermissionBase64Deflated.trim().isEmpty())
			return Collections.EMPTY_LIST;

		return userModulesUpdPermission != null ? userModulesUpdPermission
				: (userModulesUpdPermission = Strings.commaDelimitedListToSet(
						new String(CompressionCodecs.DEFLATE
								.decompress(TextCodec.BASE64URL.decode(userModulesUpdPermissionBase64Deflated)),
								Strings.UTF_8)));
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getUserModulesDelPermission() {
		if (userModulesDelPermissionBase64Deflated == null || userModulesDelPermissionBase64Deflated.trim().isEmpty())
			return Collections.EMPTY_LIST;

		return userModulesDelPermission != null ? userModulesDelPermission
				: (userModulesDelPermission = Strings.commaDelimitedListToSet(
						new String(CompressionCodecs.DEFLATE
								.decompress(TextCodec.BASE64URL.decode(userModulesDelPermissionBase64Deflated)),
								Strings.UTF_8)));
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getUserModulesVerPermission() {
		if (userModulesVerPermissionBase64Deflated == null || userModulesVerPermissionBase64Deflated.trim().isEmpty())
			return Collections.EMPTY_LIST;

		return userModulesVerPermission != null ? userModulesVerPermission
				: (userModulesVerPermission = Strings.commaDelimitedListToSet(
						new String(CompressionCodecs.DEFLATE
								.decompress(TextCodec.BASE64URL.decode(userModulesVerPermissionBase64Deflated)),
								Strings.UTF_8)));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{userName:");
		builder.append(userName);
		builder.append(", token:");
		builder.append(token);
		builder.append(", bpmToken:");
		builder.append(bpmToken);
		builder.append(", expiration:");
		builder.append(expiration);
		builder.append(", userGroupsBase64Deflated:");
		builder.append(userGroupsBase64Deflated);
		builder.append(", userModulesInsPermissionBase64Deflated:");
		builder.append(userModulesInsPermissionBase64Deflated);
		builder.append(", userModulesUpdPermissionBase64Deflated:");
		builder.append(userModulesUpdPermissionBase64Deflated);
		builder.append(", userModulesDelPermissionBase64Deflated:");
		builder.append(userModulesDelPermissionBase64Deflated);
		builder.append(", userModulesVerPermissionBase64Deflated:");
		builder.append(userModulesVerPermissionBase64Deflated);
		builder.append(", userGroups:");
		builder.append(userGroups);
		builder.append(", userModulesInsPermission:");
		builder.append(userModulesInsPermission);
		builder.append(", userModulesUpdPermission:");
		builder.append(userModulesUpdPermission);
		builder.append(", userModulesDelPermission:");
		builder.append(userModulesDelPermission);
		builder.append(", userModulesVerPermission:");
		builder.append(userModulesVerPermission);
		builder.append("}");
		return builder.toString();
	}
}
