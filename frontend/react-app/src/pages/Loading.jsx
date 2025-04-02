import '../css/Loading.css'
import { motion } from "framer-motion"
const circleStyle={
    top: '0',
    left: '0',
    display:'block',
    width: '5rem',
    height: '5rem',
    border: '0.5rem solid #e9e9e9',
    borderTop: '0.5rem solid #5d5d81',
    borderRadius: '50%',
    position: 'absoloute',
    boxSizing: 'border-box'

}
const spinTransition = {
    repeat: Infinity,
    duration: 1,
    ease: "linear",
  }
function Loading() {
    return (
        <div className="loadingContainer">
            <motion.span
                style={circleStyle}
                animate={{ rotate: 360 }}
                transition={spinTransition}
/>
        </div>
    )
}

/**
 * ==============   Styles   ================
 */
function StyleSheet() {
    return (
        <style>
            {`
            .container {
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 40px;
                border-radius: 8px;
            }

            .spinner {
                width: 50px;
                height: 50px;
                border-radius: 50%;
                border: 4px solid var(--divider);
                border-top-color: #ff0088;
                will-change: transform;
            }
            `}
        </style>
    )
}

export default Loading
