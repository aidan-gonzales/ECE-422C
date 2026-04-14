// Board.jsx — Renders a 10×10 Battleship grid.
//
// Props:
//   cells        — 2D array (10×10) of cell codes (see App.jsx for the encoding)
//   interactive  — boolean; if true, unknown cells are clickable
//   onFire(r, c) — called when the user clicks an unknown cell

import React from "react"; // see if this breaks the autograder or not

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

//   return (
//     <div style={{ display: "inline-block" }}>
//       {/* TODO */}
//       <p style={{ color: "#546e7a" }}>Board not yet implemented</p>
//     </div>
//   );

  return (
      <div style={{ display: "inline-block", userSelect: "none" }}>
        <div
          style={{
            display: "grid",
            // 11 columns total: 1 for the letters, 10 for the board cells
            gridTemplateColumns: "30px repeat(10, 32px)",
            gap: "2px",
            background: "#1a3a5a", // Dark border color
            padding: "4px",
            borderRadius: "6px",
          }}
        >
          {/* 1. Top-left empty corner cell */}
          <div></div>

          {/* 2. Column Headers (1-10) */}
          {COLS.map((col) => (
            <div
              key={col}
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                color: "#90caf9",
                fontWeight: "bold",
                fontSize: "14px",
              }}
            >
              {col}
            </div>
          ))}

          {/* 3. The Rows */}
          {cells.map((row, r) => (
            <React.Fragment key={ROWS[r]}>

              {/* Row Label (A-J) */}
              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  color: "#90caf9",
                  fontWeight: "bold",
                  fontSize: "14px",
                }}
              >
                {ROWS[r]}
              </div>

              {/* The 10 Cells in this row */}
              {row.map((val, c) => {
                // Determine visuals based on cell value and interactivity
                let bg = "#0d1b2a"; // Default dark water (not interactive)
                let cursor = "default";
                let content = "";

                if (val === 0 && interactive) {
                  bg = "#1565c0"; // Clickable blue water
                  cursor = "pointer";
                } else if (val === 1) {
                  bg = "#2e7d32"; // Ship cell (Green)
                } else if (val === 2) {
                  bg = "#c62828"; // Hit (Red)
                  content = "💥";
                } else if (val === 3) {
                  bg = "#546e7a"; // Miss (Grey)
                  content = "·";
                }

                return (
                  <div
                    key={`${r}-${c}`}
                    // ONLY trigger fire if it's my turn (interactive) AND the cell is water (0)
                    onClick={() => {
                      if (interactive && val === 0) {
                        onFire(r, c);
                      }
                    }}
                    style={{
                      width: "32px",
                      height: "32px",
                      background: bg,
                      cursor: cursor,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      fontSize: "16px",
                      color: "#ffffff",
                      borderRadius: "2px",
                      transition: "background 0.2s"
                    }}
                  >
                    {content}
                  </div>
                );
              })}
            </React.Fragment>
          ))}
        </div>
      </div>
    );

}
