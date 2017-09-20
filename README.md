# Prerequisites
 * JDK 1.7+
 * Maven 4+

# How to run
```
$ mvn test -DdiggerTargetUrl=http://example.com -DdiggerUsername=username -DdiggerPassword=password [OPTIONS]
```

## Options
In CLI prepend your options with -D
* **diggerTargetUrl** - url of the target Build Farm.
* **diggerUsername** - username for the target Build Farm.
* **diggerPassword** - password for the target Build Farm.
* **groups** - _optional_ - comma separated list of groups that should be tested. E.g: `-Dgroups=smoke,other`
* **excludedGroups** - _optional_ - comma separated list of groups that should not be tested. E.g: `-DexcludedGroups=hybrid,android` - this would only test native iOS templates and other non-android tests.
* **prefix** - _optional_ - prefix used for names of the test entities. Default value is in pom.xml.
* **fhtaUrl** - _optional_ - Git url to feedhenry/fh-template-apps fork that should be used for downloading templates
* **fhtaBranch** - _optional_ - branch/tag of feedhenry/fh-template-apps that should be used for downloading templates


# Adding self-signed certificate to your Java keystore
```
export JENKINS_URL="jenkins.hostname.tld:443"
export CERT_ALIAS="my-self-signed-jenkins"
openssl s_client -showcerts -connect $JENKINS_URL < /dev/null | openssl x509 -outform DER > tmp_cert.der
sudo keytool -import -alias $CERT_ALIAS -keystore $JAVA_HOME/jre/lib/security/cacerts -file tmp_cert.der
```

# Development
In order to make changes in this project and test it in a project which extends this one, 
do `mvn install -DskipTests=true`.

# Releasing

We release a JAR for this project to extend it. For example, people at Red Hat Mobile Application Platform (RHMAP)
are extending this project to run tests for productized RHMAP templates.

As this is a testing project, running the tests during the release doesn't make sense.

Just release it as:

TODO
```
   mvn release

``` 

# Extending

In order to extend this project to hook in your own test cases, you need to add a dependency to
this project's artifact and then define a `testng.xml` file. In your `testng.xml` file, make sure
you define `parent-module` with a Guice `Module` which you implemented.

You can override things like templates to test, environment variables etc. 
See `DiggerFeedHenryTemplatesTestModule`, a default implementation, as an example.

# Design decisions

* No static data provider for TestNG: We use dependency injection with Guice and TestNG to make 
  it easy for the other projects to extend the code. Static things are horrible to extend.
  
* Pluggable environment: In order to make use of different environment variables (or system props)
  in projects that extend this one, reading environment variables is made pluggable. 