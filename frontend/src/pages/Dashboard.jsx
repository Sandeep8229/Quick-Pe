import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { walletAPI, transactionAPI } from '../services/api';
import { ArrowUpRight, ArrowDownLeft, Plus } from 'lucide-react';
import '../styles/components.css';

/**
 * Dashboard Component - Main dashboard with balance and quick actions
 */
const Dashboard = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [balance, setBalance] = useState(null);
  const [recentTransactions, setRecentTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [balanceRes, transactionRes] = await Promise.all([
        walletAPI.getBalance(),
        transactionAPI.getHistory(),
      ]);

      setBalance(balanceRes.data.data);
      setRecentTransactions(transactionRes.data.data?.slice(0, 5) || []);
    } catch (err) {
      console.error('Failed to fetch dashboard data:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div style={{ padding: '40px 20px', maxWidth: '1200px', margin: '0 auto' }}>
      {/* Welcome Section */}
      <div style={{ marginBottom: '40px' }}>
        <h1 style={{ fontSize: '32px', marginBottom: '8px' }}>Welcome back, {user?.username}! 👋</h1>
        <p style={{ color: 'var(--text-secondary)' }}>Manage your digital wallet and send money instantly</p>
      </div>

      {/* Balance Card */}
      <div className="card" style={{ marginBottom: '40px', background: 'linear-gradient(135deg, var(--primary-color), var(--secondary-color))', color: 'white', border: 'none' }}>
        <p style={{ fontSize: '14px', opacity: 0.9 }}>Available Balance</p>
        <h2 style={{ fontSize: '48px', fontWeight: 'bold', margin: '10px 0' }}>₹ {balance || '0.00'}</h2>
        <p style={{ fontSize: '12px', opacity: 0.8 }}>Your wallet balance</p>
      </div>

      {/* Quick Actions */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginBottom: '40px' }}>
        <button className="card" onClick={() => navigate('/send-money')} style={{ cursor: 'pointer', textAlign: 'center' }}>
          <ArrowUpRight size={32} style={{ color: 'var(--primary-color)', margin: '0 auto 12px' }} />
          <p style={{ fontWeight: '600' }}>Send Money</p>
          <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Transfer to contacts</p>
        </button>

        <button className="card" onClick={() => navigate('/add-money')} style={{ cursor: 'pointer', textAlign: 'center' }}>
          <Plus size={32} style={{ color: 'var(--success-color)', margin: '0 auto 12px' }} />
          <p style={{ fontWeight: '600' }}>Add Money</p>
          <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Top up your wallet</p>
        </button>

        <button className="card" onClick={() => navigate('/transactions')} style={{ cursor: 'pointer', textAlign: 'center' }}>
          <ArrowDownLeft size={32} style={{ color: 'var(--info-color)', margin: '0 auto 12px' }} />
          <p style={{ fontWeight: '600' }}>Transactions</p>
          <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>View history</p>
        </button>
      </div>

      {/* Recent Transactions */}
      <div>
        <h3 style={{ fontSize: '20px', fontWeight: '600', marginBottom: '20px' }}>Recent Transactions</h3>
        {recentTransactions.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '40px' }}>
            <p style={{ color: 'var(--text-secondary)' }}>No transactions yet</p>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {recentTransactions.map((txn) => (
              <div key={txn.id} className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                  <div style={{
                    width: '40px',
                    height: '40px',
                    borderRadius: '50%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: txn.type === 'SENT' ? 'rgba(239, 68, 68, 0.1)' : 'rgba(16, 185, 129, 0.1)'
                  }}>
                    {txn.type === 'SENT' ? <ArrowUpRight size={20} color="#ef4444" /> : <ArrowDownLeft size={20} color="#10b981" />}
                  </div>
                  <div>
                    <p style={{ fontWeight: '600' }}>{txn.type === 'SENT' ? 'Sent to' : 'Received from'} {txn.type === 'SENT' ? txn.receiver?.username : txn.sender?.username}</p>
                    <p style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>{new Date(txn.createdAt).toLocaleDateString()}</p>
                  </div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <p style={{ fontWeight: '600', color: txn.type === 'SENT' ? '#ef4444' : '#10b981' }}>
                    {txn.type === 'SENT' ? '-' : '+'} ₹{txn.amount}
                  </p>
                  <span className={`badge badge-${txn.status === 'COMPLETED' ? 'success' : 'warning'}`}>
                    {txn.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
