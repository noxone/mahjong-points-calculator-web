import org.olafneumann.mahjong.points.game.Combination
import org.olafneumann.mahjong.points.game.GameModifiers
import org.olafneumann.mahjong.points.game.Hand
import org.olafneumann.mahjong.points.game.Tile
import org.olafneumann.mahjong.points.game.Wind
import org.olafneumann.mahjong.points.lang.StringKeys
import org.olafneumann.mahjong.points.model.CalculatorModel
import org.olafneumann.mahjong.points.model.Figure
import kotlin.test.Test
import kotlin.test.assertEquals

class HandTest {
    @Test
    fun testSimpleTileSelectionForFirstTile() {
        // Just select a first tile.
        val model = CalculatorModel(
            hand = Hand(),
            gameModifiers = GameModifiers(prevailingWind = Wind.East),
            seatWind = Wind.East,
            selectedFigure = Figure.Figure1
        )
        val tileToSelect = Tile.Bamboo1
        val expected = CalculatorModel(
            hand = Hand(
                figure1 = Combination(
                    type = Combination.Type.Unfinished0,
                    tile = tileToSelect,
                    visibility = Combination.Visibility.Open
                )
            )
        )

        val actual = model.select(tile = tileToSelect).first

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun testTileSelectionIfInvalidTile() {
        // Select a tile that does not fit to the already selected tiles
        val hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Unfinished0,
                tile = Tile.Character4,
                visibility = Combination.Visibility.Open
            )
        )
        val model = CalculatorModel(hand = hand)
        val tileToSelect = Tile.Character7
        val expectedHand = hand
        val expectedMessage = StringKeys.ERR_TILES_DOES_NOT_FIT_TO_CHOW_OR_PONG

        val actualResult = model.select(tile = tileToSelect)

        assertEquals(expected = expectedHand, actual = actualResult.first.hand)
        assertEquals(expected = expectedMessage, actual = actualResult.second.first().message)
    }

    @Test
    fun testTileSelectionIfTileIsUsedFourTimes1() {
        // select a tile that is already in use 4 times
        val hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles2,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles3,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Unfinished0,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            )
        )
        val model = CalculatorModel(hand = hand, selectedFigure = Figure.Figure3)
        val tileToSelect = Tile.Circles2

        val actual = model.select(tile = tileToSelect)

        assertEquals(expected = StringKeys.ERR_NO_TILES_LEFT_FOR_CHOW, actual = actual.second.first().message)
        assertEquals(expected = Tile.Circles3, actual = actual.second.first().tile)
    }

    @Test
    fun testTileSelectionIfTileIsUsedFourTimes2() {
        // select a tile that is already in use 4 times
        val hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles2,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles3,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Unfinished0,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            ),
        )
        val model = CalculatorModel(hand = hand, selectedFigure = Figure.Figure3)
        val tileToSelect = Tile.Circles3

        val actual = model.select(tile = tileToSelect)

        assertEquals(expected = StringKeys.ERR_NO_TILES_LEFT_FOR_CHOW, actual = actual.second.first().message)
        assertEquals(expected = Tile.Circles3, actual = actual.second.first().tile)
    }

    @Test
    fun testTileSelectionIfTileIsUsedFourTimes3() {
        // select a tile for a pung
        val hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles2,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles3,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Unfinished0,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            )
        )
        val model = CalculatorModel(hand = hand, selectedFigure = Figure.Figure3)
        val expectedHand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles2,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles3,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            )
        )
        val expectedModel = CalculatorModel(hand = expectedHand, selectedFigure = Figure.Figure3)
        val tileToSelect = Tile.Circles1

        val actualModel = model.select(tile = tileToSelect).first

        assertEquals(expected = expectedModel, actual = actualModel)
    }

    @Test
    fun testTileSelectionIfTileIsUsedFourTimes4() {
        // select a tile for a kang. The selected figure should change.
        val hand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles2,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles3,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            ),
        )
        val model = CalculatorModel(hand = hand, selectedFigure = Figure.Figure3)
        val expectedHand = Hand(
            figure1 = Combination(
                type = Combination.Type.Chow,
                tile = Tile.Circles2,
                visibility = Combination.Visibility.Open
            ),
            figure2 = Combination(
                type = Combination.Type.Pung,
                tile = Tile.Circles3,
                visibility = Combination.Visibility.Open
            ),
            figure3 = Combination(
                type = Combination.Type.Kang,
                tile = Tile.Circles1,
                visibility = Combination.Visibility.Open
            ),
        )
        val expectedModel = CalculatorModel(hand = expectedHand, selectedFigure = Figure.Figure4)
        val tileToSelect = Tile.Circles1

        val actualModel = model.select(tile = tileToSelect).first

        assertEquals(expected = expectedModel, actual = actualModel)
    }
}
