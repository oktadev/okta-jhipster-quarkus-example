# Fast Java with Quarkus and JHipster

This example app shows how to build a basic application with Java, Quarkus, and JHipster. Please read [Fast Java Made Easy with Quarkus and JHipster](https://developer.okta.com/blog/2021/03/08/jhipster-quarkus-oidc) to see how to deploy it to Heroku.

**Prerequisites:**

- [Java 11](https://adoptopenjdk.net/)+
- [Node 12](https://nodejs.org/)+
- [Docker](https://docs.docker.com/get-docker/)
- [Okta CLI 0.8.0+](https://cli.okta.com)

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage and secure users and roles in any application.

- [Getting Started](#getting-started)
- [Links](#links)
- [Help](#help)
- [License](#license)

## Getting Started

To install this example application, run the following commands:

```bash
git clone https://github.com/oktadeveloper/okta-jhipster-quarkus-example.git
cd okta-jhipster-quarkus-example
```

You can also create it by installing Quarkus for JHipster, JHipster, and creating an app with OAuth 2.0 / OIDC for authentication.

```
npm i -g generator-jhipster generator-jhipster-micronaut
mkdir fastjava && cd fastjava
jhipster
```

Start Keycloak in a Docker container:

```
docker-compose -f src/main/docker/keycloak.yml up -d
```

Then, start the app.

```
./mvnw
```

You'll be able to login with `admin/admin`.

### Use Okta for Authentication

If you'd like to use Okta instead of Keycloak, you'll need to change a few things. First, install the [Okta CLI](https://github.com/okta/okta-cli) and run `okta register` to create an account.

Once you've verified your account, run `okta apps create jhipster`. Accept the pre-selected Redirect URIs. You should see output like the following:

```
$ okta apps create jhipster
Application name [okta-jhipster-quarkus-example]:
Redirect URI
Common defaults:
  Spring Security - http://localhost:8080/login/oauth2/code/okta
  Quarkus OIDC - http://localhost:8080/callback
  JHipster - http://localhost:8080/login/oauth2/code/oidc
Enter your Redirect URI(s) [http://localhost:8080/login/oauth2/code/oidc, http://localhost:8761/login/oauth2/code/oidc]:
Enter your Post Logout Redirect URI(s) [http://localhost:8080/, http://localhost:8761/]:
Configuring a new OIDC Application, almost done:
Created OIDC application, client-id: 0oa5ozjxyNQPPbKc65d6
Creating Authorization Server claim 'groups':
Adding user daniel.petisme@gmail.com to groups: [ROLE_USER, ROLE_ADMIN]
Creating group: ROLE_USER
Creating group: ROLE_ADMIN
Okta application configuration has been written to: /Users/daniel/workspace/okta-jhipster-quarkus-example/.okta.en
```

**NOTE:** The `http://localhost:8761*` redirect URIs are for the JHipster Registry, which is often used when creating microservices with JHipster. The Okta CLI adds these by default. They aren't necessary for this tutorial, but there's no harm in leaving them in.

The Okta CLI will create an `.okta.env` in the current directory. If you look at it, you'll see that it contains several OIDC-related keys and values. 

```shell
$ cat .okta.env
export QUARKUS_OIDC_AUTH_SERVER_URL="https://dev-9323263.okta.com/oauth2/default"
export QUARKUS_OIDC_CLIENT_ID="0oa5ozjxyNQPPbKc65d6"
export QUARKUS_OIDC_CREDENTIALS_SECRET="KEJ0oNOTFEUEFHP7i1TELLING1xLm1XPRn"
export QUARKUS_OIDC_AUTHENTICATION_REDIRECT_PATH="/login/oauth2/code/oidc"
export JHIPSTER_OIDC_LOGOUT_URL="https://dev-9323263.okta.com/oauth2/default/v1/logout"
```

Source the file to set environment variables and start your application with Maven.

```shell
source .okta.env
./mvnw
```

Once it's started, open an incognito window to `http://localhost:8080` and sign in. You'll be prompted for your Okta credentials. 

After authenticating successfully, you'll be redirected back to your app. You should see your email address displayed on the homepage.

The Okta CLI streamlines JHipster's configuration and does several things for you:

1. It creates an OIDC app with the correct redirect URIs
2. It makes `ROLE_ADMIN` and `ROLE_USER` groups that JHipster expects
3. It adds your current user to the `ROLE_ADMIN` and `ROLE_USER` groups
4. It creates a `groups` claim in your default authorization server and adds the user's groups to it

You can also just use Okta's developer console to configure your app. This repo's [blog post](https://developer.okta.com/blog/2021/03/08/jhipster-quarkus-oidc) shows you how to do that.

## Links

This example uses the following open source libraries:

- [JHipster](https://jhipster.tech)
- [Quarkus](https://quarkus.io)
- [JHipster Quarkus blueprint](https://www.jhipster.tech/blueprints/quarkus/)

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2021/03/08/jhipster-quarkus-oidc), or visit our [Okta Developer Forums](https://devforum.okta.com/). You can also ask them on [Stack Overflow with the `jhipster` tag](https://stackoverflow.com/tags/jhipster).

## License

Apache 2.0, see [LICENSE](LICENSE).
