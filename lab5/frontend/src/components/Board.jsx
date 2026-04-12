// Board.jsx — Renders a 10×10 Battleship grid.
//
// Props:
//   cells        — 2D array (10×10) of cell codes (see App.jsx for the encoding)
//   interactive  — boolean; if true, unknown cells are clickable
//   onFire(r, c) — called when the user clicks an unknown cell

const COLS = ["1","2","3","4","5","6","7","8","9","10"];
const ROWS  = ["A","B","C","D","E","F","G","H","I","J"];

export default function Board({ cells, interactive, onFire }) {
  if (!cells || cells.length === 0) return null;

  // TODO: for each cell, determine the appropriate background color / content:
  //   code 0 + interactive → clickable (blue)
  //   code 0 + not interactive → empty water (dark)
  //   code 1 → ship cell (green)
  //   code 2 → hit (red, show "💥" or similar)
  //   code 3 → miss (grey, show "·" or similar)

  // TODO: render column headers (1–10) across the top
  // TODO: for each row, render a row label (A–J) then 10 cells
  // TODO: attach an onClick handler to each cell that calls onFire(r, c)
  //       only when interactive is true and cells[r][c] === 0

  return (
    <div style={{ display: "inline-block" }}>
      {/* TODO */}
      <p style={{ color: "#546e7a" }}>Board not yet implemented</p>
    </div>
  );
}
