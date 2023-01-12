# Mahjong Points Calculator [![Build and publish project](https://github.com/noxone/mahjong-point-calculator/actions/workflows/publish-project.yml/badge.svg?event=push)](https://github.com/noxone/mahjong-point-calculator/actions/workflows/publish-project.yml)

``Mahjong Point Calculator`` is a tool to compute the points of a Mahjong party. In a second mode it will be able to track the points of all players of a whole Mahjong game.

``Mahjong`` (麻將) is a Chinese game for four players. Computing the points of a Mahjong party can be quite challanging so this tool will help you here.

## Usage

### Online

Once the calculator is ready and working it will be available online and you will find a link here.

### Docker

There will also be a Docker based variant of the calculator that will support the same features as the actual website. The link will be published here, too.

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

## More Ideas

Feel free to propose new features or ideas via the [Github issues](https://github.com/noxone/mahjong-point-calculator/issues).
