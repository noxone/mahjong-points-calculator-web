import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.model.CalculatorModel
import org.olafneumann.mahjong.points.result.PlayerResult
import kotlin.test.Test
import kotlin.test.assertEquals

class DeutscheMahjongLigaResultComputerTests {
    private fun check(
        hand: Hand,
        gameModifiers: GameModifiers? = null,
        seatWind: Wind? = null,
        finalResult: Int,
        points: Int? = null,
        doublings: Int? = null
    ) {
        val model = CalculatorModel(
            hand = hand,
            seatWind = seatWind ?: Wind.East,
            gameModifiers = gameModifiers ?: GameModifiers(prevailingWind = Wind.East)
        )
        val result = model.result
        check(result = result, finalResult = finalResult, points = points, doublings = doublings)
    }

    private fun check(result: PlayerResult, finalResult: Int, points: Int? = null, doublings: Int? = null) {
        assertEquals(expected = finalResult, actual = result.result, message = "result")
        points?.let { assertEquals(expected = it, actual = result.points, message = "points") }
        doublings?.let { assertEquals(expected = it, actual = result.doublings, message = "doublings") }
    }

    @Test
    fun testComputation_simpleBonusTilesFlowers() {
        var hand = Hand(bonusTiles = setOf(Tile.Flower1))
        check(hand = hand, finalResult = 4, points = 4, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Flower1, Tile.Flower2))
        check(hand = hand, finalResult = 8, points = 8, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Flower1, Tile.Flower2, Tile.Flower3))
        check(hand = hand, finalResult = 12, points = 12, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Flower1, Tile.Flower2, Tile.Flower3, Tile.Flower4))
        check(hand = hand, finalResult = 32, points = 16, doublings = 1)
    }

    @Test
    fun testComputation_simpleBonusTilesSeasons() {
        var hand = Hand(bonusTiles = setOf(Tile.Season1))
        check(hand = hand, finalResult = 4, points = 4, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Season1, Tile.Season2))
        check(hand = hand, finalResult = 8, points = 8, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Season1, Tile.Season2, Tile.Season3))
        check(hand = hand, finalResult = 12, points = 12, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Season1, Tile.Season2, Tile.Season3, Tile.Season4))
        check(hand = hand, finalResult = 32, points = 16, doublings = 1)
    }

    @Test
    fun testComputation_simpleBonusTilesAll() {
        var hand = Hand(bonusTiles = setOf(Tile.Season1, Tile.Flower1))
        var model = CalculatorModel(hand = hand, seatWind = Wind.West)
        check(result = model.result, finalResult = 8, points = 8, doublings = 0)
        model = CalculatorModel(hand = hand, seatWind = Wind.East)
        check(result = model.result, finalResult = 16, points = 8, doublings = 1)

        hand = Hand(bonusTiles = setOf(Tile.Season1, Tile.Flower2))
        check(hand = hand, finalResult = 8, points = 8, doublings = 0)

        hand = Hand(bonusTiles = setOf(Tile.Season1, Tile.Season2, Tile.Season3, Tile.Season4, Tile.Flower3))
        check(hand = hand, finalResult = 40, points = 20, doublings = 1)

        hand = Hand(
            bonusTiles = setOf(
                Tile.Season1,
                Tile.Season2,
                Tile.Season3,
                Tile.Season4,
                Tile.Flower1,
                Tile.Flower2,
                Tile.Flower3,
                Tile.Flower4
            )
        )
        check(hand = hand, finalResult = 256, points = 32, doublings = 3)
    }

    private fun createPairHand(tile: Tile): Hand {
        return Hand(
            pair = Combination(
                type = Combination.Type.FinishingPair,
                tile = tile,
                visibility = Combination.Visibility.Open
            )
        )
    }

    @Test
    fun testComputation_pairs() {
        var hand = createPairHand(tile = Tile.Bamboo4)
        check(hand = hand, finalResult = 0)

        hand = createPairHand(tile = Tile.DragonRed)
        check(hand = hand, finalResult = 2, points = 2, doublings = 0)

        hand = createPairHand(tile = Tile.DragonGreen)
        check(hand = hand, finalResult = 2, points = 2, doublings = 0)

        hand = createPairHand(tile = Tile.DragonWhite)
        check(hand = hand, finalResult = 2, points = 2, doublings = 0)

        hand = createPairHand(tile = Tile.WindEast)
        check(
            hand = hand,
            gameModifiers = GameModifiers(prevailingWind = Wind.East),
            seatWind = Wind.East,
            finalResult = 4,
            points = 4,
            doublings = 0
        )

        // Paar des Rundenwindes
        hand = createPairHand(tile = Tile.WindWest)
        check(
            hand = hand,
            gameModifiers = GameModifiers(prevailingWind = Wind.West),
            seatWind = Wind.North,
            finalResult = 2,
            points = 2,
            doublings = 0
        )

        // Paar des Platzwindes
        hand = createPairHand(tile = Tile.WindNorth)
        check(
            hand = hand,
            gameModifiers = GameModifiers(prevailingWind = Wind.South),
            seatWind = Wind.North,
            finalResult = 2,
            points = 2,
            doublings = 0
        )

        hand = createPairHand(tile = Tile.WindWest)
        check(
            hand = hand,
            gameModifiers = GameModifiers(prevailingWind = Wind.North),
            seatWind = Wind.South,
            finalResult = 0,
            points = 0,
            doublings = 0
        )
    }

    @Test
    fun testSimpleHands() {
        var hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Bamboo3,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Character4,
                visibility = Combination.Visibility.Closed
            ),
            figure3 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Character2,
                visibility = Combination.Visibility.Open
            ),
            figure4 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            )
        )
        check(hand = hand, finalResult = 0)

        hand = hand.copy(
            pair = Combination(
                type = Combination.Type.FinishingPair,
                tile = Tile.Bamboo1,
                visibility = Combination.Visibility.Open
            )
        )
        check(hand = hand, finalResult = 20)

        hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Bamboo5,
                visibility = Combination.Visibility.Closed
            ),
            figure2 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Character5,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Kang,
                tile = Tile.DragonRed,
                visibility = Combination.Visibility.Closed
            ),
            figure4 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.WindEast,
                visibility = Combination.Visibility.Closed
            ),
            pair = Combination(
                type = Combination.Type.FinishingPair,
                tile = Tile.WindWest,
                visibility = Combination.Visibility.Open
            ),
            bonusTiles = setOf(Tile.Season1, Tile.Season2, Tile.Season3, Tile.Season4)
        )
        check(
            hand = hand,
            gameModifiers = GameModifiers(
                prevailingWind = Wind.West,
                schlussziegelVonMauer = true,
                mitDemLetztenZiegelDerMauerGewonnen = true
            ),
            seatWind = Wind.East,
            finalResult = 2688,
            points = 84,
            doublings = 5
        )
    }

    @Test
    fun testPairStuff() {
        // schlussziegel komplettiert paar aus Grundziegeln
        var hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Bamboo3,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Character4,
                visibility = Combination.Visibility.Closed
            ),
            figure3 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Character2,
                visibility = Combination.Visibility.Open
            ),
            figure4 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            ),
            pair = Combination(
                type = Combination.Type.FinishingPair,
                tile = Tile.Circles7,
                visibility = Combination.Visibility.Open
            )
        )
        check(
            hand = hand,
            gameModifiers = GameModifiers(prevailingWind = Wind.East, schlussziegelKomplettiertPaar = true),
            finalResult = 22
        )

        // schlussziegel komplettiert paar aus Hauptziegeln
        hand = hand.copy(
            pair = Combination(
                type = Combination.Type.FinishingPair,
                tile = Tile.Character9,
                visibility = Combination.Visibility.Open
            )
        )
        check(
            hand = hand,
            gameModifiers = GameModifiers(prevailingWind = Wind.East, schlussziegelKomplettiertPaar = true),
            finalResult = 24
        )
    }
}
