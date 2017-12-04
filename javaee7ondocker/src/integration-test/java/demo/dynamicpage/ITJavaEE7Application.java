package demo.dynamicpage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * Integration test suite to verify the deployment of JavaEE7 Web application on Apache Tomcat Application Server.
 *
 */
public class ITJavaEE7Application {

	/**
	 * Integration test to perform health of the Apache Tomcat Application Server and validate the existence of
	 * one of the dynamic pages.
	 */
	@Test
	public void testDynamicPage() throws Exception {
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		try {
			final HttpResponse httpResponse1 = verifyAndReturnResponse(httpclient, "/javaee7ondocker/");
			final HttpEntity entity1 = httpResponse1.getEntity();
			if (entity1 != null) {
				Assert.assertFalse(EntityUtils.toString(entity1).isEmpty());
			}
		} catch (final Exception exception) {
			exception.printStackTrace();
			Assert.fail("Failed validation of javaee war deployment");
		} finally {
			httpclient.close();;
		}
	}

	/**
	 * Helper method to validate the status code of the http server response and the http response headers and
	 * return an instance of HttpResponse.
	 * @param httpclient HttpClient instance.
	 * @param relativePath relative path of the static content url.
	 * @return an instance of HttpResponse.
	 * @throws Exception on failure.
	 */
	private static HttpResponse verifyAndReturnResponse(final HttpClient httpclient, final String relativePath)
			throws ParseException, IOException {
		// specify the host, protocol, and port
		final HttpHost target = new HttpHost(getDockerHostname(), 8091, "http");
		// specify the get request
		final HttpGet getRequest = new HttpGet(relativePath);
		final HttpResponse httpResponse = httpclient.execute(target, getRequest);
		Assert.assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
		validateResponseHeaders(httpResponse);
		return httpResponse;
	}

	/**
	 * Helper method to validate the http response headers for the server information and content type.
	 * @param httpResponse instance of HttpResponse.
	 */
	private static void validateResponseHeaders(HttpResponse httpResponse) {
		final Header[] headers1 = httpResponse.getAllHeaders();
		Assert.assertTrue(headers1.length > 0);
		final List<Header> headerList = Arrays.asList(headers1);
		System.out.println(headerList);
        Assert.assertTrue(headerList.stream().anyMatch(
                h -> "Content-Type".equals(h.getName())
        ));
	}

	/**
	 * Helper method to get the name of docker host.
	 * @return the name of docker host.
	 */
	private static String getDockerHostname() {
		String hostname = "";
		try {
			final String dockerHostString = System.getenv().get("DOCKER_HOST");
			if (dockerHostString == null || dockerHostString.isEmpty()) {
				throw new Exception("No environment variable DOCKER_HOST defined");
			}
			// Split host string into protocol, domain and port.
			final Pattern pattern = Pattern.compile("(tcp?://)([^:^/]*)(:\\d*)?");
			final Matcher matcher = pattern.matcher(dockerHostString);

			if (!matcher.find()) {
				throw new Exception("Invalid DOCKER_HOST value");
			}
			validateDockerHost();
			hostname = matcher.group(2);
		} catch (final Exception exception) {
			exception.printStackTrace();
			Assert.fail("Failed getting the docker url");
		}
		return hostname;
	}

	/**
	 * Validate Docker Host based on "DOCKER_HOST" env variable using remote APIs. 
	 */
	private static void validateDockerHost() {
		final String dockerHostString = System.getenv().get("DOCKER_HOST");
		final DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
			    .withDockerHost(dockerHostString)
			    .withDockerTlsVerify(true)
			    .build();
		final DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
		final List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
		Assert.assertFalse(containers.isEmpty());
        Assert.assertTrue(containers.stream().anyMatch(
                c -> c.getCommand().startsWith("catalina.sh run") &&
                "docker-demo/javaee7ondocker".equals(c.getImage()) &&
                validateContainerPorts(c) &&
                c.getStatus().startsWith("Up ")
        ));
	}

	/**
	 * Helper method to validate the container port type, container public port and container private port.
	 * @param container instance of Container.
	 * @return true if all the asserts have passed.
	 */
	private static boolean validateContainerPorts(final Container container) {
		final ContainerPort[] ports = container.getPorts();
		Assert.assertEquals(1, ports.length);
		final ContainerPort port = ports[0];
		Assert.assertEquals("tcp", port.getType());
		Assert.assertEquals(8080, port.getPrivatePort().intValue());
		Assert.assertEquals(8091, port.getPublicPort().intValue());
		return true;
	}
}
