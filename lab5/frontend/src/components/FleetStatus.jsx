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

  return (
    <div>
      <h4>{label}</h4>
      {/* TODO */}
      <p style={{ color: "#546e7a", fontSize: 12 }}>Fleet status not yet implemented</p>
    </div>
  );
}
