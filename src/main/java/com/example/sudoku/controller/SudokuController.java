package com.example.sudoku.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sudoku.service.SudokuService;

@RestController
public class SudokuController {
	@Autowired
    private SudokuService sudokuService;
    
	@GetMapping("/api/puzzle")
    public Map<String, int[][]> getNewPuzzle() {
        
    	// Generate a new puzzle
        int[][] puzzle = sudokuService.generatePuzzle();
        
        // Get the solution for the generated puzzle
        int[][] solution = sudokuService.getSolution(puzzle);
        
        // Prepare the response map
        Map<String, int[][]> response = new HashMap<>();
        response.put("puzzle", puzzle);
        response.put("solution", solution);
        
        return response;
    }
}
