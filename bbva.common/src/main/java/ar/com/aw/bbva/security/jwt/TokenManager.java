package ar.com.aw.bbva.security.jwt;
import ar.com.aw.bbva.business.exception.AccessTokenExeption;
import ar.com.aw.bbva.context.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import io.jsonwebtoken.lang.Strings;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class TokenManager {
	public static final String			TOKEN_HEADER_NAME	= "X-AUTHENTICATION-TOKEN";
	private final SignatureAlgorithm	signatureAlgorithm	= SignatureAlgorithm.HS256;
	
	private String tokenPrivateKey;
	private long ttlMillis = 1000 * 60 * 60 * 9;		// 9hs
	
	public TokenManager(String tokenPrivateKey, Long ttlMillis) {
		super();
		this.tokenPrivateKey = tokenPrivateKey;
		this.ttlMillis = ttlMillis;
	}

	public UserContext getUserContextByToken(String token){
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(tokenPrivateKey))
					.parseClaimsJws(token).getBody();
			
			return new UserContext(
					claims.getSubject(), 
					token,
					(String) claims.get("bpmToken"),
					(String) claims.get("accessToken"),
					claims.getExpiration(),
					(String) claims.get("userGroups"),
					(String) claims.get("modulosPerIns"),
					(String) claims.get("modulosPerUpd"),
					(String) claims.get("modulosPerDel"),
					(String) claims.get("modulosPerVer")
			);
		} catch (SignatureException e) {
			throw new AccessTokenExeption(e);
		}
    }
	
	public String createNewToken(String userName, String bpmToken, Collection<String> userGroups,
			Collection<String> modulosPerIns,
			Collection<String> modulosPerUpd,
			Collection<String> modulosPerDel,
			Collection<String> modulosPerVer) {
		Random rn = new Random();
		int id = rn.nextInt(1000000);
		String strId = String.valueOf(id);

    	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenPrivateKey);
    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    	JwtBuilder builder = Jwts.builder()
    			.setId(strId).setSubject(userName)
    	        .setIssuedAt(new Date())
    	        .claim("bpmToken", bpmToken)
    	        .claim("accessToken", createNewToken(userName))
    	        .claim("userGroups", TextCodec.BASE64URL.encode(
    	        		CompressionCodecs.DEFLATE.compress(
    	        				Strings.collectionToCommaDelimitedString(userGroups)
    	        				.getBytes(Strings.UTF_8)))
    	        		)
    	        .claim("modulosPerIns", TextCodec.BASE64URL.encode(
    	        		CompressionCodecs.DEFLATE.compress(
    	        				Strings.collectionToCommaDelimitedString(modulosPerIns)
    	        				.getBytes(Strings.UTF_8)))
    	        		)
    	        .claim("modulosPerUpd", TextCodec.BASE64URL.encode(
    	        		CompressionCodecs.DEFLATE.compress(
    	        				Strings.collectionToCommaDelimitedString(modulosPerUpd)
    	        				.getBytes(Strings.UTF_8)))
    	        		)
    	        .claim("modulosPerDel", TextCodec.BASE64URL.encode(
    	        		CompressionCodecs.DEFLATE.compress(
    	        				Strings.collectionToCommaDelimitedString(modulosPerDel)
    	        				.getBytes(Strings.UTF_8)))
    	        		)
    	        .claim("modulosPerVer", TextCodec.BASE64URL.encode(
    	        		CompressionCodecs.DEFLATE.compress(
    	        				Strings.collectionToCommaDelimitedString(modulosPerVer)
    	        				.getBytes(Strings.UTF_8)))
    	        		)
    	        .signWith(signatureAlgorithm, signingKey);

    	if (ttlMillis >= 0) {
    	    long expMillis = System.currentTimeMillis() + ttlMillis;
    	    Date exp = new Date(expMillis);
    	    builder.setExpiration(exp);
    	}
    	
    	return builder.compact();
	}
	
	private String createNewToken(String userName) {
		Random rn = new Random();
		int id = rn.nextInt(1000000);
		String strId = String.valueOf(id);

    	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenPrivateKey);
    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    	JwtBuilder builder = Jwts.builder()
    			.setId(strId).setSubject(userName)
    	        .setIssuedAt(new Date())
    	        .signWith(signatureAlgorithm, signingKey);

    	if (ttlMillis >= 0) {
    	    long expMillis = System.currentTimeMillis() + ttlMillis;
    	    Date exp = new Date(expMillis);
    	    builder.setExpiration(exp);
    	}
    	
    	return builder.compact();
	}
	
//	public static void main(String args[]) {
//		
//		Random rn = new Random();
//		int id = rn.nextInt(1000000);
//		String strId = String.valueOf(id);
//
//    	byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("privatekey");
//    	Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
//		JwtBuilder builder = Jwts.builder()
////				.compressWith(CompressionCodecs.GZIP)
//				.setId(strId).setSubject("weblogic")
//                .setIssuedAt(new Date())
//                .signWith(SignatureAlgorithm.HS256, signingKey);
//		
//		Collection<String> roles = new ArrayList<String>();
//		for (int i = 0; i < 1000; i++)
//			roles.add("ROL_GRUPO_PRINCIPAL"+i);
//		long time = System.nanoTime();
//		
////		builder.claim("userContext",new UserContext("gdelasilva", "token", "bpmToken", new Date(), TextCodec.BASE64URL.encode(CompressionCodecs.DEFLATE.compress(Strings.collectionToCommaDelimitedString(roles).getBytes(Strings.UTF_8)))));
//		builder.claim("roles", TextCodec.BASE64URL.encode(CompressionCodecs.DEFLATE.compress("[{\"signature\":\"caXs8c7IpORoT2PwtzCBnw==\",\"salt\":\"MTQ2NDk3Mjk5MDgwNA==\",\"name\":\"tadmin\",\"dn\":\"uid=tadmin,ou=people,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD60F40FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"dGFkbWlu\"},{\"signature\":\"LLKT6+g+WWXVbFQQ2OANHg==\",\"salt\":\"MTQ2NDk3Mjk5MDcxMw==\",\"name\":\"Administrators\",\"dn\":\"cn=Administrators,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CCF5880FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QWRtaW5pc3RyYXRvcnM=\"},{\"signature\":\"Bin4HZNT2lC6DRKldj1ffQ==\",\"salt\":\"MTQ2NDk3Mjk5MDcxNA==\",\"name\":\"AdminChannelUsers\",\"dn\":\"cn=AdminChannelUsers,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD46190FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QWRtaW5DaGFubmVsVXNlcnM=\"},{\"signature\":\"7fAkdWrS5R6gUXoew2V0Sw==\",\"salt\":\"MTQ2NDk3Mjk5MDcxNQ==\",\"name\":\"AppTesters\",\"dn\":\"cn=AppTesters,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD2B3E0FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QXBwVGVzdGVycw==\"},{\"signature\":\"zPYJZsIvbhzKu5L/TkPJkg==\",\"salt\":\"MTQ2NDk3Mjk5MDcxNg==\",\"name\":\"BPM_GCS_CONTRATISTAS\",\"dn\":\"cn=BPM_GCS_CONTRATISTAS,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"F5027CF0FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX0dDU19DT05UUkFUSVNUQVM=\"},{\"signature\":\"9WguYWCl4ngWg/xJQlwWhQ==\",\"salt\":\"MTQ2NDk3Mjk5MDcxOA==\",\"name\":\"BPM_RS_Analista Administracion\",\"dn\":\"cn=BPM_RS_Analista Administracion,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"F4991D80FDA311E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0FuYWxpc3RhIEFkbWluaXN0cmFjaW9u\"},{\"signature\":\"S3qSj+A48nPkFUZio7Pnkw==\",\"salt\":\"MTQ2NDk3Mjk5MDcyMQ==\",\"name\":\"BPM_RS_Analista Sitios\",\"dn\":\"cn=BPM_RS_Analista Sitios,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"6B6B82E0FDA411E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0FuYWxpc3RhIFNpdGlvcw==\"},{\"signature\":\"b8xwlSoThRJAKxsVYqdVsA==\",\"salt\":\"MTQ2NDk3Mjk5MDcyMg==\",\"name\":\"BPM_RS_Jefe Sitios\",\"dn\":\"cn=BPM_RS_Jefe Sitios,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"5865A600FDB611E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0plZmUgU2l0aW9z\"},{\"signature\":\"WiK3V8g8Ka5vwVhPWlE7RQ==\",\"salt\":\"MTQ2NDk3Mjk5MDcyMw==\",\"name\":\"BPM_RS_Legales\",\"dn\":\"cn=BPM_RS_Legales,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"1C647E40FDA411E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xlZ2FsZXM=\"},{\"signature\":\"zw70HztaRBYtpQtvUo0LKw==\",\"salt\":\"MTQ2NDk3Mjk5MDcyNA==\",\"name\":\"BPM_RS_Legales AMBA\",\"dn\":\"cn=BPM_RS_Legales AMBA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"AFE5C500FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xlZ2FsZXMgQU1CQQ==\"},{\"signature\":\"YUKyNDWwJv+t0O5FvYVXRw==\",\"salt\":\"MTQ2NDk3Mjk5MDcyNg==\",\"name\":\"BPM_RS_Legales INTERIOR\",\"dn\":\"cn=BPM_RS_Legales INTERIOR,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"A0E6CF90FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xlZ2FsZXMgSU5URVJJT1I=\"},{\"signature\":\"lPHRgUfgUu/xKQf3r15suA==\",\"salt\":\"MTQ2NDk3Mjk5MDcyNw==\",\"name\":\"BPM_RS_Lider Gestoria AERIALS\",\"dn\":\"cn=BPM_RS_Lider Gestoria AERIALS,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"8EC26090FDA611E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIEFFUklBTFM=\"},{\"signature\":\"8i5QoIRnXg7/x+EezcO8pA==\",\"salt\":\"MTQ2NDk3Mjk5MDcyOQ==\",\"name\":\"BPM_RS_Lider Gestoria CAS SRL\",\"dn\":\"cn=BPM_RS_Lider Gestoria CAS SRL,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"75758000FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIENBUyBTUkw=\"},{\"signature\":\"4qfyxk79iFauTR7voTI5wA==\",\"salt\":\"MTQ2NDk3Mjk5MDczMQ==\",\"name\":\"BPM_RS_Lider Gestoria Cinetik SRL\",\"dn\":\"cn=BPM_RS_Lider Gestoria Cinetik SRL,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"33E941D0FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIENpbmV0aWsgU1JM\"},{\"signature\":\"xFLff2CO4ezQ26/7qWMwfQ==\",\"salt\":\"MTQ2NDk3Mjk5MDczNA==\",\"name\":\"BPM_RS_Lider Gestoria Claro AMBA\",\"dn\":\"cn=BPM_RS_Lider Gestoria Claro AMBA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"BCF7F480FD9111E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIENsYXJvIEFNQkE=\"},{\"signature\":\"X058FaCnbNRd0/HbqQ3KEw==\",\"salt\":\"MTQ2NDk3Mjk5MDczNQ==\",\"name\":\"BPM_RS_Lider Gestoria Claro Interior\",\"dn\":\"cn=BPM_RS_Lider Gestoria Claro Interior,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"CD98DA20FD9111E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIENsYXJvIEludGVyaW9y\"},{\"signature\":\"Bvd4xUAllI8P9OM1ej+Icw==\",\"salt\":\"MTQ2NDk3Mjk5MDczNg==\",\"name\":\"BPM_RS_Lider Gestoria Florencia Giacobbe\",\"dn\":\"cn=BPM_RS_Lider Gestoria Florencia Giacobbe,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"6C442310FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIEZsb3JlbmNpYSBHaWFjb2JiZQ==\"},{\"signature\":\"7QmrtexACiggo9LyVnd10Q==\",\"salt\":\"MTQ2NDk3Mjk5MDczOA==\",\"name\":\"BPM_RS_Lider Gestoria Geolink\",\"dn\":\"cn=BPM_RS_Lider Gestoria Geolink,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"4C7F81F0FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIEdlb2xpbms=\"},{\"signature\":\"7lsqyE2E4M0qhbYDR1lZ6g==\",\"salt\":\"MTQ2NDk3Mjk5MDczOQ==\",\"name\":\"BPM_RS_Lider Gestoria Lancervif\",\"dn\":\"cn=BPM_RS_Lider Gestoria Lancervif,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"0F06F740FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIExhbmNlcnZpZg==\"},{\"signature\":\"ErvF0QM5YkmL2v2eoLScyg==\",\"salt\":\"MTQ2NDk3Mjk5MDc0MA==\",\"name\":\"BPM_RS_Lider Gestoria Lopez Faienza\",\"dn\":\"cn=BPM_RS_Lider Gestoria Lopez Faienza,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"8E601720FDB811E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIExvcGV6IEZhaWVuemE=\"},{\"signature\":\"/BAta9tDN6Ou8rNMg+m98Q==\",\"salt\":\"MTQ2NDk3Mjk5MDc0Mg==\",\"name\":\"BPM_RS_Lider Gestoria Luis Cornaglia\",\"dn\":\"cn=BPM_RS_Lider Gestoria Luis Cornaglia,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"05F5F390FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIEx1aXMgQ29ybmFnbGlh\"},{\"signature\":\"8Dd9g4REgmv21zHlOH3Chg==\",\"salt\":\"MTQ2NDk3Mjk5MDc0Mw==\",\"name\":\"BPM_RS_Lider Gestoria LyC\",\"dn\":\"cn=BPM_RS_Lider Gestoria LyC,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"FD037460FDB811E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIEx5Qw==\"},{\"signature\":\"Dp8ZfiF/v1nvHZTBoNCO6Q==\",\"salt\":\"MTQ2NDk3Mjk5MDc0NA==\",\"name\":\"BPM_RS_Lider Gestoria TYE AMBA\",\"dn\":\"cn=BPM_RS_Lider Gestoria TYE AMBA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"5A3A1080FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIFRZRSBBTUJB\"},{\"signature\":\"9HafbG2wX+jj2tJLLWfIvg==\",\"salt\":\"MTQ2NDk3Mjk5MDc0NQ==\",\"name\":\"BPM_RS_Lider Gestoria TYE Interior\",\"dn\":\"cn=BPM_RS_Lider Gestoria TYE Interior,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"8E54D710FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIFRZRSBJbnRlcmlvcg==\"},{\"signature\":\"+ZV7bL1Q2muDUDbU4LrlTw==\",\"salt\":\"MTQ2NDk3Mjk5MDc0Ng==\",\"name\":\"BPM_RS_Lider Gestoria Villella SA\",\"dn\":\"cn=BPM_RS_Lider Gestoria Villella SA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"8069C2A0FDB911E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0xpZGVyIEdlc3RvcmlhIFZpbGxlbGxhIFNB\"},{\"signature\":\"A46KCsItmyZMvVExS7/zBw==\",\"salt\":\"MTQ2NDk3Mjk5MDc0OA==\",\"name\":\"BPM_RS_Negociador Claro AMBA\",\"dn\":\"cn=BPM_RS_Negociador Claro AMBA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"D7C67160FD9111E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX05lZ29jaWFkb3IgQ2xhcm8gQU1CQQ==\"},{\"signature\":\"+6b6DeaNU1xA8Ds28Vb61w==\",\"salt\":\"MTQ2NDk3Mjk5MDc0OQ==\",\"name\":\"BPM_RS_Negociador Claro Interior\",\"dn\":\"cn=BPM_RS_Negociador Claro Interior,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"DD48D010FD9111E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX05lZ29jaWFkb3IgQ2xhcm8gSW50ZXJpb3I=\"},{\"signature\":\"bnJ5IP0DiS/1dCMVGBa94w==\",\"salt\":\"MTQ2NDk3Mjk5MDc1MQ==\",\"name\":\"BPM_RS_Supervisor Administracion\",\"dn\":\"cn=BPM_RS_Supervisor Administracion,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"80C0D490FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX1N1cGVydmlzb3IgQWRtaW5pc3RyYWNpb24=\"},{\"signature\":\"m67DHCFYZ/HubG9tPSd/XA==\",\"salt\":\"MTQ2NDk3Mjk5MDc1MQ==\",\"name\":\"BPM_RS_Supervisores AMBA\",\"dn\":\"cn=BPM_RS_Supervisores AMBA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"852AD9B0FDA411E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX1N1cGVydmlzb3JlcyBBTUJB\"},{\"signature\":\"JWjly5c0yCpZLo9dcSP08w==\",\"salt\":\"MTQ2NDk3Mjk5MDc1Mg==\",\"name\":\"BPM_RS_Supervisores INTERIOR\",\"dn\":\"cn=BPM_RS_Supervisores INTERIOR,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"A8C00AD0FDA411E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX1N1cGVydmlzb3JlcyBJTlRFUklPUg==\"},{\"signature\":\"2fNRNY3z3iVQhxEuhpcAqg==\",\"salt\":\"MTQ2NDk3Mjk5MDc1Mw==\",\"name\":\"CrossDomainConnectors\",\"dn\":\"cn=CrossDomainConnectors,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD37730FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"Q3Jvc3NEb21haW5Db25uZWN0b3Jz\"},{\"signature\":\"gl+rSg9+ZM8GkGOsLHEYhQ==\",\"salt\":\"MTQ2NDk3Mjk5MDc1NA==\",\"name\":\"Deployers\",\"dn\":\"cn=Deployers,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD042E0FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"RGVwbG95ZXJz\"},{\"signature\":\"oC5ETXTb4LOaiUPDuY/3wg==\",\"salt\":\"MTQ2NDk3Mjk5MDc1Ng==\",\"name\":\"Monitors\",\"dn\":\"cn=Monitors,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD1C980FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"TW9uaXRvcnM=\"},{\"signature\":\"/+f2bk2bW5EPzXXM5kiyyg==\",\"salt\":\"MTQ2NDk3Mjk5MDc1OQ==\",\"name\":\"OC_ESP_IMP\",\"dn\":\"cn=OC_ESP_IMP,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"EBFFF830FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"T0NfRVNQX0lNUA==\"},{\"signature\":\"6vBMhOnqV0OPTfVvXcDsMA==\",\"salt\":\"MTQ2NDk3Mjk5MDc2MA==\",\"name\":\"OC_ESP_INFRA\",\"dn\":\"cn=OC_ESP_INFRA,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"F04FA340FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"T0NfRVNQX0lORlJB\"},{\"signature\":\"dvs9IrkD3SNVOXI4wQdf8Q==\",\"salt\":\"MTQ2NDk3Mjk5MDc2Mg==\",\"name\":\"OC_GERENTE_IMPLANTACION\",\"dn\":\"cn=OC_GERENTE_IMPLANTACION,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"FA5B3390FDBF11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"T0NfR0VSRU5URV9JTVBMQU5UQUNJT04=\"},{\"signature\":\"L0RxWHRAfp9w0jiFyxpu7A==\",\"salt\":\"MTQ2NDk3Mjk5MDc2Mw==\",\"name\":\"Operators\",\"dn\":\"cn=Operators,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"9CD10630FD3011E5BFEA996C37F8E781\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"T3BlcmF0b3Jz\"},{\"signature\":\"/58nQOmbDLNXcKeCIHmXig==\",\"salt\":\"MTQ2NDk3Mjk5MDc2NQ==\",\"name\":\"ALSBSystemGroup\",\"dn\":\"cn=ALSBSystemGroup,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"A5F19F10FF4511E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QUxTQlN5c3RlbUdyb3Vw\"},{\"signature\":\"YmkKtgkYmm8o73XvxGsgBg==\",\"salt\":\"MTQ2NDk3Mjk5MDc2Nw==\",\"name\":\"OracleSystemGroup\",\"dn\":\"cn=OracleSystemGroup,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"79A4D4E0FF4511E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"T3JhY2xlU3lzdGVtR3JvdXA=\"},{\"signature\":\"sojg+bdbzajLcINJrgJvXA==\",\"salt\":\"MTQ2NDk3Mjk5MDc2OQ==\",\"name\":\"IntegrationAdministrators\",\"dn\":\"cn=IntegrationAdministrators,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"2C5F60F0FF4611E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"SW50ZWdyYXRpb25BZG1pbmlzdHJhdG9ycw==\"},{\"signature\":\"+2+j1qAzkEO7l87OAMO6Dw==\",\"salt\":\"MTQ2NDk3Mjk5MDc3MA==\",\"name\":\"IntegrationDeployers\",\"dn\":\"cn=IntegrationDeployers,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"32D917F0FF4611E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"SW50ZWdyYXRpb25EZXBsb3llcnM=\"},{\"signature\":\"AaRcOCuHHbL9RE5F3677BA==\",\"salt\":\"MTQ2NDk3Mjk5MDc3MQ==\",\"name\":\"IntegrationMonitors\",\"dn\":\"cn=IntegrationMonitors,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"38B88AC0FF4611E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"SW50ZWdyYXRpb25Nb25pdG9ycw==\"},{\"signature\":\"4Ej8AqU25Kk3cHZ9GGZIFw==\",\"salt\":\"MTQ2NDk3Mjk5MDc3Mg==\",\"name\":\"IntegrationOperators\",\"dn\":\"cn=IntegrationOperators,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"40848B50FF4611E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"SW50ZWdyYXRpb25PcGVyYXRvcnM=\"},{\"signature\":\"MwXJc+c+61xx65EHnYHTeQ==\",\"salt\":\"MTQ2NDk3Mjk5MDc3Mg==\",\"name\":\"AUTHENTICATED_USER\",\"dn\":\"cn=AUTHENTICATED_USER,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"34B6E650299011E6BFD317BA516869DC\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QVVUSEVOVElDQVRFRF9VU0VS\"},{\"signature\":\"tbCFuVl6u6GC8o4k7aX7PA==\",\"salt\":\"MTQ2NDk3Mjk5MDc3Mw==\",\"name\":\"BPM_RS_Gerencia Sitios\",\"dn\":\"cn=BPM_RS_Gerencia Sitios,ou=groups,ou=myrealm,dc=Trimix_domain\",\"guid\":\"ADE75260FDBE11E5BFD71B8E3CEF0F9D\",\"equalsCaseInsensitive\":false,\"equalsCompareDnAndGuid\":false,\"principalFactoryCreated\":true,\"signedData\":\"QlBNX1JTX0dlcmVuY2lhIFNpdGlvcw==\"}]".getBytes(Strings.UTF_8))));
//		System.out.println(System.nanoTime() - time);
//		if (900000 >= 0) {
//			long expMillis = System.currentTimeMillis() + 900000;
//			Date exp = new Date(expMillis);
//			builder.setExpiration(exp);
//		}
//
//		String token = builder.compact();
//		System.out.println(token);
//
//		Claims claims = Jwts.parser()
//				.setSigningKey(DatatypeConverter.parseBase64Binary("privatekey"))
//				.parseClaimsJws(token).getBody();
//		
//		System.out.println("******");
//		System.out.println(claims.get("userContext"));
//		System.out.println("******");
//		System.out.println(claims.keySet());
//		time = System.nanoTime();
//		String userGroups = new String(CompressionCodecs.DEFLATE.decompress(TextCodec.BASE64URL.decode(claims.get("roles").toString())),Strings.UTF_8);
//		System.out.println(System.nanoTime() - time);
//		System.out.println(Strings.commaDelimitedListToSet(userGroups));
//		
//		System.out.println(claims.values());
//	}
	
//	public byte[] encripta(String sinEncriptar) throws Exception {
//		final byte[] bytes = sinEncriptar.getBytes();
//		final Cipher aes = obtieneCipher(true);
//		final byte[] encriptado = aes.doFinal(bytes);
//		return encriptado;
//	}
//
//	public String desencripta(byte[] encriptado) throws Exception {
//		final Cipher aes = obtieneCipher(false);
//		final byte[] bytes = aes.doFinal(encriptado);
//		final String sinEncriptar = new String(bytes);
//		return sinEncriptar;
//	}
//
//	private Cipher obtieneCipher(boolean paraEncriptar) throws Exception {
//		final String frase = "desadesadesadesa";
//		final MessageDigest digest = MessageDigest.getInstance("SHA");
//		digest.update(frase.getBytes());
//		final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
//
//		final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
//		aes.init(paraEncriptar?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE, key);
//
//		return aes;
//	}
//	
//	public static void main(String... args) throws Exception {
//		TokenManager tm = new TokenManager("asds", 0L);
//		String mensaje = "HOLAAAAAAAAAAAAA";
//		byte[] encriptado = tm.encripta(mensaje);
//		String desencriptado = tm.desencripta(encriptado);
//		System.out.println("Mensaje:"+mensaje);
//		System.out.println("Cifrado: "+new String(encriptado));
//		System.out.println("Deencriptado:" + desencriptado);
//		
//		encriptado = tm.encripta(mensaje);
//		System.out.println("Mensaje:" + mensaje);
//		System.out.println("Cifrado: " + new String(encriptado));
//		desencriptado = tm.desencripta(encriptado);
//		System.out.println("Deencriptado:" + desencriptado);
//
//		System.out.println("BASE64-------------------------");
//		String encriptadoBase64 = Base64Codec.BASE64.encode(encriptado);
//		System.out.println("CifradoBase64: " + encriptadoBase64);
//		desencriptado = tm.desencripta(Base64Codec.BASE64.decode(encriptadoBase64));
//		System.out.println("DesencriptadoBase64:" + desencriptado);
//		
//	}
}
