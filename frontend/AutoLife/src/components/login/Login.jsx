import { useState } from 'react'
import { API_BASE_URL } from "../../config/api.js";
import './Login.css'
import { toast } from "react-toastify";
import iconAutolife from "../../assets/images/hospital.svg"

function Login({ onLoginSuccess }) {
    const [isLogin, setIsLogin] = useState(true)
    const [formData, setFormData] = useState({
        login: '',
        senha: '',
    })
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    const handleChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }))

        if (error) setError('')
    }

    const handleLogin = async (e) => {
        e.preventDefault()
        setError('')
        setLoading(true)

        try {

            const response = await fetch(`${API_BASE_URL}/usuarios/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    login: formData.login,
                    senha: formData.senha
                })
            })

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}))
                toast.error(errorData.message || 'Credenciais inválidas')
            }

            const data = await response.json()

            localStorage.setItem('token', data.token)
            localStorage.setItem('login', formData.login)

            onLoginSuccess(data.token, formData.login)

        } catch (err) {
            toast.error('Erro no login:', err)
            toast.error(err.message || 'Erro ao realizar login. Verifique suas credenciais.')
        } finally {
            setLoading(false)
        }
    }

    const handleRegistro = async (e) => {
        e.preventDefault()
        setError('')
        setLoading(true)

        try {

            const response = await fetch(`${API_BASE_URL}/usuarios`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    login: formData.login,
                    senha: formData.senha
                })
            })

            const data = await response.json()

            if (!response.ok) {
                toast.error(data.message || 'Erro ao registrar usuário')
                return
            }

            toast.success(`✅ Usuário "${data.login}" registrado com sucesso! Fazendo login...`)

            setFormData({ login: '', senha: '', email: '' })

            setIsLogin(true)

        } catch (err) {
            toast.error(err.message || 'Erro ao registrar usuário. Tente novamente.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="login-container">
            <div className="login-background"/>
            <div className="login-card">
                <div className="login-header">
                    <div className="login-icon">
                        <span>
                             <img className="login-img" src={ iconAutolife } alt="Simbolo AutoLife"/>
                        </span>
                    </div>
                    <h1 className="login-title">AutoLife</h1>
                    <p className="login-subtitle">
                        {isLogin ? 'Faça login para acessar o sistema' : 'Crie sua conta'}
                    </p>
                </div>

                <div className="login-tabs">
                    <button
                        className={`login-tab ${isLogin ? 'active' : ''}`}
                        onClick={() => {
                            setIsLogin(true)
                            setError('')
                            setFormData({ login: '', senha: '' })
                        }}
                        disabled={loading}
                    >
                        Login
                    </button>
                    <button
                        className={`login-tab ${!isLogin ? 'active' : ''}`}
                        onClick={() => {
                            setIsLogin(false)
                            setError('')
                            setFormData({ login: '', senha: '' })
                        }}
                        disabled={loading}
                    >
                        Registrar
                    </button>
                </div>

                <form onSubmit={isLogin ? handleLogin : handleRegistro} className="login-form">
                    <div className="form-group">
                        <label className="form-label">
                            Usuário
                        </label>
                        <input
                            type="text"
                            className="form-input"
                            value={formData.login}
                            onChange={(e) => handleChange('login', e.target.value)}
                            placeholder="Digite seu usuário"
                            required
                            disabled={loading}
                            autoFocus
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label">

                            Senha
                        </label>
                        <input
                            type="password"
                            className="form-input"
                            value={formData.senha}
                            onChange={(e) => handleChange('senha', e.target.value)}
                            placeholder="Digite sua senha"
                            required
                            disabled={loading}
                        />
                    </div>

                    {error && (
                        <div className="error-message">
                            <span className="error-icon">⚠️</span>
                            {error}
                        </div>
                    )}

                    <button
                        type="submit"
                        className="login-button"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="loading-spinner-small"></span>
                                {isLogin ? 'Entrando...' : 'Registrando...'}
                            </>
                        ) : (
                            <>
                                {isLogin ? 'Entrar' : 'Criar Conta'}
                            </>
                        )}
                    </button>
                </form>

                <div className="login-footer">
                    <p className="footer-text">
                        {isLogin ? 'Não tem uma conta?' : 'Já tem uma conta?'}
                        <button
                            type="button"
                            className="footer-link"
                            onClick={() => {
                                setIsLogin(!isLogin)
                                setError('')
                                setFormData({ login: '', senha: '' })
                            }}
                            disabled={loading}
                        >
                            {isLogin ? 'Registre-se aqui' : 'Faça login'}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    )
}

export default Login