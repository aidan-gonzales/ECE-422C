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
  useEffect(() => {
      if (bottomRef.current) {
        bottomRef.current.scrollIntoView({ behavior: "smooth" });
      }
    }, [log]);

  const submit = () => {
    // TODO: call onSend with the trimmed input, then clear the input field
    //       do nothing if the trimmed string is empty
    const trimmed = input.trim();
    if (trimmed !== "") {
      onSend(trimmed);
      setInput(""); // Clear the input box after sending
    }
  };

  // TODO: render:
  //   - a scrollable list of messages (from, time, text)
  //   - a text input that calls submit() on Enter key
  //   - a Send button that calls submit() on click

//   return (
//     <div>
//       <h3>💬 Chat</h3>
//       {/* TODO */}
//       <p style={{ color: "#546e7a", fontSize: 13 }}>Chat not yet implemented</p>
//     </div>
//   );

  return (
      <div style={{
        display: "flex",
        flexDirection: "column",
        height: "450px", // Fixed height so it matches the boards nicely
        background: "#1a3a5a",
        borderRadius: "8px",
        padding: "16px",
        border: "1px solid #2a4a6a"
      }}>
        <h3 style={{ marginTop: 0, color: "#90caf9", borderBottom: "1px solid #2a4a6a", paddingBottom: "10px" }}>
          💬 Chat
        </h3>

        {/* Scrollable message list */}
        <div style={{ flex: 1, overflowY: "auto", margin: "12px 0", paddingRight: "8px" }}>
          {log.length === 0 ? (
            <p style={{ color: "#546e7a", fontSize: "13px", fontStyle: "italic" }}>
              No messages yet. Say hello!
            </p>
          ) : (
            log.map((msg, idx) => (
              <div key={idx} style={{ marginBottom: "12px" }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "baseline", marginBottom: "4px" }}>
                  <strong style={{ color: "#a5d6a7", fontSize: "14px" }}>{msg.from}</strong>
                  <span style={{ color: "#546e7a", fontSize: "11px" }}>{msg.time}</span>
                </div>
                <div style={{ color: "#e0e0e0", fontSize: "14px", lineHeight: "1.4", wordBreak: "break-word" }}>
                  {msg.text}
                </div>
              </div>
            ))
          )}
          {/* The invisible target we scroll to */}
          <div ref={bottomRef} />
        </div>

        {/* Input area */}
        <div style={{ display: "flex", gap: "8px" }}>
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            // Trigger the submit function if they press the Enter key
            onKeyDown={(e) => e.key === "Enter" && submit()}
            placeholder="Type a message..."
            style={{
              flex: 1,
              padding: "10px",
              background: "#0d1b2a",
              border: "1px solid #2a4a6a",
              borderRadius: "4px",
              color: "#e0e0e0",
              outline: "none"
            }}
          />
          <button
            onClick={submit}
            style={{
              padding: "10px 16px",
              background: "#1565c0",
              border: "none",
              borderRadius: "4px",
              color: "white",
              cursor: "pointer",
              fontWeight: "bold"
            }}
          >
            Send
          </button>
        </div>
      </div>
    );
}
