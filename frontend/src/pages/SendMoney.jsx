import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { transactionAPI } from '../services/api';
import { AlertCircle, CheckCircle } from 'lucide-react';
import '../styles/components.css';

/**
 * SendMoney Component - Send money to other users
 */
const SendMoney = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    receiverIdentifier: '',
    amount: '',
    description: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const response = await transactionAPI.sendMoney({
        ...formData,
        amount: parseFloat(formData.amount),
      });

      if (response.data.success) {
        setSuccess('Money sent successfully!');
        setFormData({ receiverIdentifier: '', amount: '', description: '' });
        setTimeout(() => navigate('/dashboard'), 2000);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to send money. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '40px 20px', maxWidth: '600px', margin: '0 auto' }}>
      <h1 style={{ fontSize: '28px', marginBottom: '30px' }}>💸 Send Money</h1>

      <div className="card">
        {error && (
          <div className="alert alert-error" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <AlertCircle size={20} />
            {error}
          </div>
        )}

        {success && (
          <div className="alert alert-success" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <CheckCircle size={20} />
            {success}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Receiver (Mobile Number or UPI ID)</label>
            <input
              type="text"
              name="receiverIdentifier"
              className="input"
              value={formData.receiverIdentifier}
              onChange={handleChange}
              required
              placeholder="9876543210 or user@upi"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Amount (₹)</label>
            <input
              type="number"
              name="amount"
              className="input"
              value={formData.amount}
              onChange={handleChange}
              required
              placeholder="100"
              min="1"
              step="0.01"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Description (Optional)</label>
            <textarea
              name="description"
              className="input"
              value={formData.description}
              onChange={handleChange}
              placeholder="Payment for..."
              rows="4"
              style={{ resize: 'vertical' }}
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-block"
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Send Money'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default SendMoney;
