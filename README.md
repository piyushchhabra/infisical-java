<h1 align="center">
    <a href="https://github.com/Infisical/infisical">
        <img width="300" src="https://raw.githubusercontent.com/Infisical/infisical-node/main/img/logoname-white.svg#gh-dark-mode-only" alt="infisical">
    </a>
</h1>
<p align="center">
  <p align="center">Open-source, end-to-end encrypted tool to manage secrets and configs across your team and infrastructure.</p>
</p>

## Table of Contents

- [Links](#links)
- [Requirements](#requirements)
- [Installation](#installation)

## Links

- [Infisical](https://github.com/Infisical/infisical)

## Requirements

- Java 1.8 or later
- Maven

## Installation

The recommended way to use the Infisical SDK for Java in your project is to consume it from Maven. Import as follows:
```xml
<dependency>
    <groupId>io.github.piyushchhabra</groupId>
    <artifactId>infisical-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration

Initialize the `InfisicalClient` with your [Infisical Token](https://infisical.com/docs/getting-started/dashboard/token).

```java
InfisicalClientOptions options = InfisicalClientOptions.builder()
    .token("YOUR_TOKEN_HERE")
    .cacheTtlInSeconds(60L)
    .debugMode(true)
    .build();

InfisicalClient client = new InfisicalClient(options);
```

### Options

| Parameter | Type     | Description |
| --------- | -------- | ----------- |
| `token`   | `string` | An Infisical Token scoped to a project and environment. |
| `siteURL` | `string` | Your self-hosted Infisical site URL. Default: `https://app.infisical.com`. |
| `cacheTtlInSeconds`| `number` | Time-to-live (in seconds) for refreshing cached secrets. Minimum: `60` and Default: `300`.|
| `debugMode`   | `boolean` | Turns debug mode on or off. Default: `false`.      |

## Contributing

Bug fixes, docs, and library improvements are always welcome. Please refer to our [Contributing Guide](https://infisical.com/docs/contributing/overview) for detailed information on how you can contribute.

[//]: contributor-faces

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->

<a href="https://github.com/piyushchhabra"><img src="https://avatars.githubusercontent.com/u/12864227?v=4" width="50" height="50" alt=""/></a>