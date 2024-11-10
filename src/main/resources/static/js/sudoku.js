let solution = [];
const board = document.getElementById('board');

// Initialize the game on page load
document.addEventListener('DOMContentLoaded', () => {
    createBoard();
    fetchPuzzle();
});


function createBoard() {
    board.innerHTML = Array.from({ length: 16 }, (_, i) => {
        const row = Math.floor(i / 4);
        const col = i % 4;
        return `<div class="cell" id="cell-${row}-${col}"></div>`;
    }).join('');
}


function displayPuzzle(puzzle) {
    puzzle.forEach((row, i) => {
        row.forEach((value, j) => {
            const cell = document.getElementById(`cell-${i}-${j}`);
            cell.innerHTML = '';
            if (value) {
                cell.classList.add('given');
                cell.textContent = value;
            } else {
                cell.classList.remove('given');
                const input = document.createElement('input');
                input.type = 'number';
                input.min = '1';
                input.max = '4';
                input.addEventListener('input', (e) => validateInput(e, input));
                cell.appendChild(input);
            }
        });
    });
}

//Validates user input for Sudoku cells
function validateInput(event, input) {
    const value = event.target.value;
    if (value < 1 || value > 4) input.value = '';
}

//Fetches a new puzzle from the server
async function fetchPuzzle() {
    try {
		createBoard();
        const response = await fetch('/api/puzzle');
        if (!response.ok) throw new Error('Nätverkssvaret var inte ok');
        const data = await response.json();
        
        const puzzle = data.puzzle;
        solution = data.solution;

        displayPuzzle(puzzle);
    } catch (error) {
        console.error('Det gick inte att hämta pussel:', error);
        alert('Det gick inte att ladda nytt pussel. Försök igen.');
    }
}

function validateBoard() {
    const cells = Array.from(document.querySelectorAll('.cell'));
    let isCorrect = true;

    cells.forEach((cell, index) => {
        const row = Math.floor(index / 4);
        const col = index % 4;
        const userValue = cell.querySelector('input') 
                           ? parseInt(cell.querySelector('input').value, 10) 
                           : parseInt(cell.textContent, 10);

        if (userValue !== solution[row][col]) {
            isCorrect = false;
            cell.classList.add('incorrect');
        } else {
            cell.classList.remove('incorrect');
        }
    });

    if (isCorrect) {
        alert('Grattis! Du löste sudoku korrekt.');
    } else {
        alert('Det finns några felaktiga svar. Försök igen.');
    }
}
