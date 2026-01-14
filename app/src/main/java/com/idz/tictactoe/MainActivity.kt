package com.idz.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.idz.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    enum class Player {
        X,
        O
    }

    private var currentPlayer = Player.X

    private var boardList = mutableListOf<Button>()

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resetBoard()
        updateTurnMessage()

        binding?.playAgainButton?.setOnClickListener {
            resetBoard()
            binding?.playAgainButton?.visibility = View.INVISIBLE
            currentPlayer = Player.X
            updateTurnMessage()
        }
    }

    private fun resetBoard() {
        boardList.clear()
        for (i in 0 until binding!!.boardLayout.childCount) {
            val row = binding!!.boardLayout.getChildAt(i)
            if (row is android.widget.LinearLayout) {
                for (j in 0 until row.childCount) {
                    val cell = row.getChildAt(j)
                    if (cell is Button) {
                        cell.text = ""
                        cell.setOnClickListener {
                            boardCellTapped(cell)
                        }

                        boardList.add(cell)
                    }
                }
            }
        }

        setBoardEnabled(true)
    }

    private fun boardCellTapped(cell: Button) {
        if (cell.text != "") {
            return
        }

        if (currentPlayer == Player.X) {
            cell.text = "X"
            cell.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            currentPlayer = Player.O
        } else {
            cell.text = "O"
            cell.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
            currentPlayer = Player.X
        }

        val winner = checkWinner()
        if (winner != null) {
            binding?.gameProgressTextView?.text = getString(R.string.player_wins, winner)
            binding?.playAgainButton?.visibility = View.VISIBLE
            setBoardEnabled(false)
        } else if (isBoardFull()) {
            binding?.gameProgressTextView?.text = getString(R.string.draw)
            binding?.playAgainButton?.visibility = View.VISIBLE
        } else {
            updateTurnMessage()
        }
    }

    private fun updateTurnMessage() {
        binding?.gameProgressTextView?.text = getString(R.string.player_turn, currentPlayer)
    }

    private fun setBoardEnabled(enabled: Boolean) {
        for (button in boardList) {
            button.isEnabled = enabled
        }
    }

    private fun checkWinner(): String? {
        val board = Array(3) { Array(3) { "" } }
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = boardList[i * 3 + j].text.toString()
            }
        }

        for (i in 0..2) {
            if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0]
            }

            if (board[0][i] != "" && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i]
            }
        }

        if (board[0][0] != "" && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }

        if (board[0][2] != "" && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }

        return null
    }

    private fun isBoardFull(): Boolean {
        for (button in boardList) {
            if (button.text == "") {
                return false
            }
        }

        return true
    }

}