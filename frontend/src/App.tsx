
import { useState } from 'react'
import type { FormEvent } from 'react'
import './App.css'

type Slot = {
  id: number
  slotDate: string
  startTime: string
  durationMinutes: number
  topic: string
  booked: boolean
  bookedByName?: string
  aiConfirmationNote?: string
}

function App() {
  const [slots, setSlots] = useState<Slot[]>([])
  const [date, setDate] = useState('')
  const [time, setTime] = useState('')
  const [duration, setDuration] = useState(30)
  const [topic, setTopic] = useState('')
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [selectedSlot, setSelectedSlot] = useState<number | null>(null)
  const [message, setMessage] = useState('')
  const [messageType, setMessageType] = useState<'success' | 'error'>('success')
  const [loading, setLoading] = useState(false)

  async function loadSlots() {
    setLoading(true)

    try {
      const response = await fetch('http://localhost:8080/api/slots')

      if (!response.ok) {
        throw new Error('Could not load sessions.')
      }

      setSlots(await response.json())
    } catch {
      setMessageType('error')
      setMessage('Could not connect to the server. Make sure the backend is running.')
    } finally {
      setLoading(false)
    }
  }

  async function createSlot(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setMessage('')

    if (!topic.trim() || !date || !time) {
      setMessageType('error')
      setMessage('Please fill in all session details.')
      return
    }

    const selectedDateTime = new Date(`${date}T${time}`)

    if (selectedDateTime <= new Date()) {
      setMessageType('error')
      setMessage('The session must be scheduled for a future date and time.')
      return
    }

    try {
      const response = await fetch('http://localhost:8080/api/slots', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          slotDate: date,
          startTime: time,
          durationMinutes: duration,
          topic: topic.trim(),
        }),
      })

      if (!response.ok) {
        throw new Error(await response.text())
      }

      setTopic('')
      setDate('')
      setTime('')
      setDuration(30)
      setMessageType('success')
      setMessage('Session created successfully.')
      await loadSlots()
    } catch (error) {
      setMessageType('error')
      setMessage(
          error instanceof Error ? error.message : 'Could not create the session.',
      )
    }
  }

  async function bookSlot(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (selectedSlot === null) {
      setMessageType('error')
      setMessage('Please select a session.')
      return
    }

    if (!name.trim() || !email.trim()) {
      setMessageType('error')
      setMessage('Please enter your name and email address.')
      return
    }

    setMessage('')

    try {
      const response = await fetch(
          `http://localhost:8080/api/slots/${selectedSlot}/book`,
          {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              name: name.trim(),
              email: email.trim(),
            }),
          },
      )

      if (!response.ok) {
        throw new Error(await response.text())
      }

      const result: Slot = await response.json()

      setMessageType('success')
      setMessage(result.aiConfirmationNote || 'Your booking has been confirmed.')
      setName('')
      setEmail('')
      setSelectedSlot(null)
      await loadSlots()
    } catch (error) {
      setMessageType('error')
      setMessage(
          error instanceof Error ? error.message : 'Could not complete the booking.',
      )
    }
  }

  const availableSlots = slots.filter(slot => !slot.booked)
  const bookedSlots = slots.filter(slot => slot.booked)

  function formatDate(value: string) {
    return new Date(`${value}T12:00:00`).toLocaleDateString('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
    })
  }

  return (
      <main className="app">
        <header className="topbar">
          <a className="brand" href="#">
            schedura<span>.</span>
          </a>
          <span className="topbar-label">SCHEDULING PLATFORM</span>
        </header>

        <section className="hero">
          <p className="eyebrow">SESSION MANAGEMENT</p>
          <h1>Schedule your next session.</h1>
          <p className="hero-description">
            Browse available sessions, select a suitable time, and confirm your booking.
          </p>
          <button className="primary-button" onClick={loadSlots} disabled={loading}>
            {loading ? 'Loading sessions...' : 'View available sessions'}
          </button>
        </section>

        {message && (
            <div className={`message ${messageType}`} role="status">
              <span>{message}</span>
              <button
                  type="button"
                  className="message-close"
                  aria-label="Dismiss message"
                  onClick={() => setMessage('')}
              >
                ×
              </button>
            </div>
        )}

        <section className="stats-grid">
          <div className="stat-card">
            <span className="stat-label">Total sessions</span>
            <strong>{slots.length}</strong>
          </div>
          <div className="stat-card">
            <span className="stat-label">Available</span>
            <strong>{availableSlots.length}</strong>
          </div>
          <div className="stat-card">
            <span className="stat-label">Booked</span>
            <strong>{bookedSlots.length}</strong>
          </div>
        </section>

        <section className="content-grid">
          <div className="panel sessions-panel">
            <div className="section-heading">
              <div>
                <p className="eyebrow">SCHEDULE</p>
                <h2>Available sessions</h2>
              </div>
              <button className="text-button" onClick={loadSlots} disabled={loading}>
                Refresh
              </button>
            </div>

            {availableSlots.length === 0 ? (
                <div className="empty-state">
                  <div className="empty-icon">—</div>
                  <h3>No available sessions</h3>
                  <p>Create a session below or refresh to check for new availability.</p>
                  <button className="text-button" onClick={loadSlots} disabled={loading}>
                    Load sessions
                  </button>
                </div>
            ) : (
                <div className="slot-list">
                  {availableSlots.map(slot => (
                      <article className="slot-card" key={slot.id}>
                        <div className="slot-date">
                          <span>{formatDate(slot.slotDate).split(' ')[1]}</span>
                          <strong>{slot.slotDate.slice(8, 10)}</strong>
                        </div>

                        <div className="slot-info">
                          <h3>{slot.topic}</h3>
                          <p>{formatDate(slot.slotDate)}</p>
                          <p>
                            {slot.startTime.slice(0, 5)} · {slot.durationMinutes} minutes
                          </p>
                        </div>

                        <button
                            className="outline-button"
                            onClick={() => setSelectedSlot(slot.id)}
                        >
                          {selectedSlot === slot.id ? 'Selected' : 'Select'}
                        </button>
                      </article>
                  ))}
                </div>
            )}

            {bookedSlots.length > 0 && (
                <div className="booked-summary">
                  <h3>Recently booked sessions</h3>
                  {bookedSlots.map(slot => (
                      <div className="booked-row" key={slot.id}>
                        <div>
                          <strong>{slot.topic}</strong>
                          <p>
                            {formatDate(slot.slotDate)} · {slot.startTime.slice(0, 5)}
                          </p>
                        </div>
                        <span className="status-pill">Booked</span>
                      </div>
                  ))}
                </div>
            )}
          </div>

          <aside className="panel booking-panel">
            <p className="eyebrow">RESERVATIONS</p>
            <h2>Book a session</h2>
            <p className="panel-description">
              Select a session and provide your details to confirm your reservation.
            </p>

            <form onSubmit={bookSlot} className="form">
              <label>
                Selected session
                <select
                    value={selectedSlot ?? ''}
                    onChange={event =>
                        setSelectedSlot(event.target.value ? Number(event.target.value) : null)
                    }
                    required
                >
                  <option value="">Choose a session</option>
                  {availableSlots.map(slot => (
                      <option key={slot.id} value={slot.id}>
                        {slot.topic} — {slot.slotDate} at {slot.startTime.slice(0, 5)}
                      </option>
                  ))}
                </select>
              </label>

              <label>
                Full name
                <input
                    value={name}
                    onChange={event => setName(event.target.value)}
                    placeholder="Enter your full name"
                    autoComplete="name"
                    required
                />
              </label>

              <label>
                Email address
                <input
                    type="email"
                    value={email}
                    onChange={event => setEmail(event.target.value)}
                    placeholder="name@example.com"
                    autoComplete="email"
                    required
                />
              </label>

              <button className="primary-button full-button" type="submit">
                Confirm booking
              </button>
            </form>
          </aside>
        </section>

        <section className="panel create-panel">
          <div>
            <p className="eyebrow">HOST CONTROLS</p>
            <h2>Create a session</h2>
            <p className="panel-description">
              Add a new session to the schedule for users to reserve.
            </p>
          </div>

          <form onSubmit={createSlot} className="form create-form">
            <label>
              Session topic
              <input
                  value={topic}
                  onChange={event => setTopic(event.target.value)}
                  placeholder="e.g. Product consultation"
                  required
              />
            </label>

            <label>
              Date
              <input
                  type="date"
                  value={date}
                  onChange={event => setDate(event.target.value)}
                  min={new Date().toLocaleDateString('en-CA')}
                  required
              />
            </label>

            <label>
              Start time
              <input
                  type="time"
                  value={time}
                  onChange={event => setTime(event.target.value)}
                  required
              />
            </label>

            <label>
              Duration
              <select
                  value={duration}
                  onChange={event => setDuration(Number(event.target.value))}
              >
                <option value={15}>15 minutes</option>
                <option value={30}>30 minutes</option>
                <option value={45}>45 minutes</option>
                <option value={60}>60 minutes</option>
              </select>
            </label>

            <button className="primary-button full-button" type="submit">
              Create session
            </button>
          </form>
        </section>

        <footer className="footer">
          <a className="brand" href="#">
            schedura<span>.</span>
          </a>
          <p>Session scheduling and booking management</p>
        </footer>
      </main>
  )
}

export default App
