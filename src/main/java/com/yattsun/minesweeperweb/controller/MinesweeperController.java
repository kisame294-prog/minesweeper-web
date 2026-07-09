package com.yattsun.minesweeperweb.controller;

import com.yattsun.minesweeperweb.domain.Board;
import com.yattsun.minesweeperweb.service.ClearTimeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MinesweeperController {
    private final ClearTimeService clearTimeService;

    public MinesweeperController(ClearTimeService clearTimeService) {
        this.clearTimeService = clearTimeService;
    }

    private Board getBoard(HttpSession session){
        Board board = (Board) session.getAttribute("board");

        if(board == null){
            board = new Board(9,9);
            session.setAttribute("board",board);
        }

        return board;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Board board = getBoard(session);

        model.addAttribute("board", board);
        model.addAttribute("gameOver", board.isGameOver());
        model.addAttribute("isClear",board.isClear());
        model.addAttribute("elapsedTime",board.getElapsedSeconds());
        model.addAttribute("timerRunning",board.isTimerRunning());
        model.addAttribute("formattedElapsedTime",board.getFormattedElapsedTime());

        return "index";
    }

    @PostMapping("/open")
    public String open(@RequestParam int y,
                       @RequestParam int x,
                       HttpSession session){

        Board board = getBoard(session);

        if(board.isClear() || board.isGameOver()){
            return "redirect:/";
        }

        board.init(y,x);
        board.openBoard(y,x);

        if(board.isClear() || board.isGameOver()){
            board.stopTimer();

            if(board.isClear()){
                clearTimeService.saveClearTime(
                        (int) board.getElapsedSeconds()
                );
            }
        }

        if (board.isGameOver()){
            board.showBombs();
        }

        return "redirect:/";
    }

    @PostMapping("/flag")
    public String flag(@RequestParam int y,
                       @RequestParam int x,
                       HttpSession session){

        Board board = getBoard(session);
        board.toggleFlag(y, x);

        return "redirect:/";
    }

    @PostMapping("/reset")
    public String reset(HttpSession session){

        Board board = new Board(9,9);

        session.setAttribute("board",board);

        return "redirect:/";
    }
}
