package com.tocaboca.tocalifew.white

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tocaboca.tocalifew.R
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

class Game : AppCompatActivity() {
    private lateinit var adapter: BoardAdapter
    private  lateinit var rvBoard: RecyclerView
    private lateinit var  tvNumPairs: TextView
    private lateinit var clRoot: ConstraintLayout
    private lateinit var game: Gamer
    private var boardSize: BoardSize = BoardSize.EASY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        clRoot = findViewById(R.id.constLayRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        setUpBoard()

    }

    private fun setUpBoard() {
        when(boardSize){
            BoardSize.EASY->{
                tvNumPairs.text = "Pairs: 0 / 4"
            }
        }

        game = Gamer(boardSize)

        adapter =  BoardAdapter(this, boardSize, game.cards, object : BoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true);
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun updateGameWithFlip(position: Int) {

        if(game.isCardFaceUp(position)){
            Toast.makeText(this, "This is an invalid move", Toast.LENGTH_SHORT).show()
        }
        if(game.flipperCard(position)){

            tvNumPairs.text = "Pairs: ${game.numPairsFound} / ${boardSize.getNumPairs()}"
            if(game.winGame()){
                MaterialAlertDialogBuilder(this@Game)
                    .setTitle("You win!")
                    .setCancelable(false)
                    .setPositiveButton("Play again"){dialog, _ ->
                        val preferences = getSharedPreferences("PREF", 0)
                        val editor = preferences.edit()
                        editor.apply()
                        startActivity(Intent(applicationContext, Game::class.java))
                        finish()
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }

        adapter.notifyDataSetChanged()

    }
}