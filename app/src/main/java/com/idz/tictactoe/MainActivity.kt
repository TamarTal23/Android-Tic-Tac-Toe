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
                            boardCellPressed(cell)
                        }

                        boardList.add(cell)
                    }
                }
            }
        }
    }

    private fun boardCellPressed(cell: Button) {
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

        if (isBoardFull()) {
            binding?.gameProgressTextView?.text = getString(R.string.draw)
            binding?.playAgainButton?.visibility = View.VISIBLE
        } else {
            updateTurnMessage()
        }
    }

    private fun updateTurnMessage() {
        binding?.gameProgressTextView?.text = getString(R.string.player_turn, currentPlayer)
    }

    private fun isBoardFull(): Boolean { // TODO check when game is a draw
        for (button in boardList) {
            if (button.text == "") {
                return false
            }
        }

        return true
    }
}