# Mahjong Points Calculator [![Build and publish project](https://github.com/noxone/mahjong-point-calculator/actions/workflows/publish-project.yml/badge.svg?event=push)](https://github.com/noxone/mahjong-point-calculator/actions/workflows/publish-project.yml)

``Mahjong Point Calculator`` is a tool to compute the points of a Mahjong game. In a second mode it will be able to track the points of all players of a whole Mahjong party.

Mahjong (麻將) is a Chinese game for four players. Computing the points of a Mahjong party can be quite challanging so this tool will help you here. 

## Usage

### Online

``Mahjong Point Calculator``is available online. Please visit [https://mahjong.olafneumann.org](https://mahjong.olafneumann.org) and use it directly in the browser.

The page is currently optimized especially for the usage on tablets. Other form factors might look very odd and not very easy to use. This might be available in later stages of the project.

### Docker

You can use ``Mahjong Point Calculator``via Docker. Please find the generated images in [this repository](https://hub.docker.com/r/noxone/mahjongpoints). Use the following command and find the application on port 80 of your local machine:

```bash
docker run -d -p 80:80 noxone/mahjongpoints
```


## Development

### Build

1. Clone the project
2. In the project's root directory execute

   ```bash
   gradlew clean build
   ```

   Or use docker to build the project:

   ```bash
   docker build . -t noxone/mahjongpoints
   ```

3. Find the output in directory ``./build/distributions/``

### Live Development

For a nice development experience use

```bash
gradlew run --continuous
```

Using this command the page will reload automatically for every source file change.

## Project Goal

I personally like to play Mahjong but calculating the points is quite cumbersome. So, to be able to track the points of several games, I started to create this project.

## Artwork

The artwork used for this page is freely available on the internet. Currently in use:

- [Tile images](./art/tiles/README.md)
- [Tile backside](./art/backside/README.md)
- [Fonts](./art/fonts/README.md)

## More Ideas

Feel free to propose new features or ideas via the [Github issues](https://github.com/noxone/mahjong-point-calculator/issues).
