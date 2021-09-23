package com.example.mytest.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardTest {

    @Test
    fun `isComplete returns false if there is one clear cell left`() {
        val board = Board(
            listOf(
                Cell(0, 0, Clear),
                Cell(0, 1, OSelected),
                Cell(0, 2, XSelected),
                Cell(1, 0, OSelected),
                Cell(1, 1, XSelected),
                Cell(1, 2, OSelected),
                Cell(2, 0, XSelected),
                Cell(2, 1, OSelected),
                Cell(2, 2, XSelected)
            )
        )
        assertFalse(board.isCompleted())
    }

    @Test
    fun `isComplete returns true if there are not clear cells left`() {
        val board = Board(
            listOf(
                Cell(0, 0, XSelected),
                Cell(0, 1, OSelected),
                Cell(0, 2, XSelected),
                Cell(1, 0, OSelected),
                Cell(1, 1, XSelected),
                Cell(1, 2, OSelected),
                Cell(2, 0, XSelected),
                Cell(2, 1, OSelected),
                Cell(2, 2, XSelected)
            )
        )
        assertTrue(board.isCompleted())
    }

    @Test
    fun `getCell in board by row and column returns cell if exists`() {
        val expectedCell = Cell(0, 0, XSelected)
        val board = Board(
            listOf(
                expectedCell,
                Cell(0, 1, OSelected),
                Cell(0, 2, XSelected),
                Cell(1, 0, OSelected),
                Cell(1, 1, XSelected),
                Cell(1, 2, OSelected),
                Cell(2, 0, XSelected),
                Cell(2, 1, OSelected),
                Cell(2, 2, XSelected)
            )
        )
        assertEquals(expectedCell, board.getCell(0, 0))
    }

    @Test
    fun `getCell in board by row and column returns null if it does not exist`() {
        val board = Board(
            listOf(
                Cell(0, 0, XSelected),
                Cell(0, 1, OSelected),
                Cell(0, 2, XSelected),
                Cell(1, 0, OSelected),
                Cell(1, 1, XSelected),
                Cell(1, 2, OSelected),
                Cell(2, 0, XSelected),
                Cell(2, 1, OSelected),
                Cell(2, 2, XSelected)
            )
        )
        assertNull(board.getCell(10, 0))
    }

    @Test
    fun `getCell in board by given cell returns cell if exists`() {
        val expectedCell = Cell(0, 0, XSelected)
        val board = Board(
            listOf(
                expectedCell,
                Cell(0, 1, OSelected),
                Cell(0, 2, XSelected),
                Cell(1, 0, OSelected),
                Cell(1, 1, XSelected),
                Cell(1, 2, OSelected),
                Cell(2, 0, XSelected),
                Cell(2, 1, OSelected),
                Cell(2, 2, XSelected)
            )
        )
        assertEquals(expectedCell, board.getCell(Cell(0, 0)))
    }

    @Test
    fun `getCell in board by given cell returns null if it does not exist`() {
        val board = Board(
            listOf(
                Cell(0, 0, XSelected),
                Cell(0, 1, OSelected),
                Cell(0, 2, XSelected),
                Cell(1, 0, OSelected),
                Cell(1, 1, XSelected),
                Cell(1, 2, OSelected),
                Cell(2, 0, XSelected),
                Cell(2, 1, OSelected),
                Cell(2, 2, XSelected)
            )
        )
        assertNull(board.getCell(Cell(10, 0)))
    }
}
