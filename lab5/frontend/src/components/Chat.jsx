// Chat.jsx — In-game chat panel.
//
// Props:
//   log            — array of { from, text, time } objects (newest last)
//   onSend(text)   — called when the user submits a message

import { useState, useRef, useEffect } from "react";

export default function Chat({ log, onSend }) {
  const [input, setInput] = useState("");
  const bottomRef = useRef(null);

  // TODO: use a useEffect to auto-scroll to bottomRef whenever log changes

  const submit = () => {
    // TODO: call onSend with the trimmed input, then clear the input field
    //       do nothing if the trimmed string is empty
  };

  // TODO: render:
  //   - a scrollable list of messages (from, time, text)
  //   - a text input that calls submit() on Enter key
  //   - a Send button that calls submit() on click

  return (
    <div>
      <h3>💬 Chat</h3>
      {/* TODO */}
      <p style={{ color: "#546e7a", fontSize: 13 }}>Chat not yet implemented</p>
    </div>
  );
}
