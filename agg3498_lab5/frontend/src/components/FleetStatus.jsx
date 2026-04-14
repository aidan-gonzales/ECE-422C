// FleetStatus.jsx — Displays the status of each ship in a fleet.
//
// Props:
//   fleet  — array of { name, size, sunk } objects
//   label  — string heading (e.g. "My Ships" or "Opponent's Ships")

export default function FleetStatus({ fleet, label }) {
  if (!fleet) return null;

  // TODO: render the label as a heading
  // TODO: for each ship, render:
  //   - a row of "pip" squares (one per ship cell) colored to indicate sunk vs intact
  //   - the ship name and size
  //   - a "SUNK" badge when ship.sunk is true
  //   - a strikethrough style on the name when sunk

//   return (
//     <div>
//       <h4>{label}</h4>
//       {/* TODO */}
//       <p style={{ color: "#546e7a", fontSize: 12 }}>Fleet status not yet implemented</p>
//     </div>
//   );

  return (
      <div
        style={{
          background: "#1a3a5a",
          borderRadius: "8px",
          padding: "16px",
          marginTop: "16px",
          border: "1px solid #2a4a6a",
          minWidth: "200px" // Keeps the box from collapsing too much
        }}
      >
        <h4
          style={{
            margin: "0 0 12px 0",
            color: "#90caf9",
            borderBottom: "1px solid #2a4a6a",
            paddingBottom: "8px",
            textTransform: "uppercase",
            letterSpacing: "1px"
          }}
        >
          {label}
        </h4>

        <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
          {fleet.map((ship, idx) => {
            // Determine styles based on whether the ship is sunk
            const textColor = ship.sunk ? "#546e7a" : "#e0e0e0";
            const textDecoration = ship.sunk ? "line-through" : "none";
            const pipColor = ship.sunk ? "#c62828" : "#2e7d32"; // Red if sunk, Green if intact

            return (
              <div key={idx} style={{ display: "flex", flexDirection: "column", gap: "4px" }}>

                {/* Ship Name, Size, and SUNK Badge */}
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                  <span style={{ color: textColor, textDecoration: textDecoration, fontSize: "14px", fontWeight: "bold" }}>
                    {ship.name} <span style={{ fontWeight: "normal", fontSize: "12px" }}>({ship.size})</span>
                  </span>

                  {ship.sunk && (
                    <span style={{
                      background: "#c62828",
                      color: "white",
                      fontSize: "10px",
                      fontWeight: "bold",
                      padding: "2px 6px",
                      borderRadius: "4px",
                      letterSpacing: "0.5px"
                    }}>
                      SUNK
                    </span>
                  )}
                </div>

                {/* Health Pips (Squares) */}
                <div style={{ display: "flex", gap: "4px" }}>
                  {/* Create an array of undefined items matching the ship's size so we can map over it */}
                  {Array.from({ length: ship.size }).map((_, pipIdx) => (
                    <div
                      key={pipIdx}
                      style={{
                        width: "14px",
                        height: "14px",
                        background: pipColor,
                        borderRadius: "2px",
                        opacity: ship.sunk ? 0.7 : 1 // Dim the pips slightly if destroyed
                      }}
                    />
                  ))}
                </div>

              </div>
            );
          })}
        </div>
      </div>
    );
}
