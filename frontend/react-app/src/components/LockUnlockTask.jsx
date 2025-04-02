import { useState } from 'react';
import { lockTask, unlockTask } from '../api/adminApi';
import { Lock, LockOpen } from 'lucide-react';
import "../css/MyTasks.css";

function LockUnlockTask({ initialIsLocked, taskId }) {
  const [isLocked, setIsLocked] = useState(initialIsLocked);

  const handleLockUnlock = async () => {
    try {
      if (isLocked) {
        const unlock = await unlockTask(taskId);
        if (unlock) {
          setIsLocked(false);
        }
      } else {
        const lock = await lockTask(taskId);
        if (lock) {
          setIsLocked(true);
        }
      }
    } catch (error) {
      console.log(`error`);
    }
  };

  return (
    <button onClick={handleLockUnlock} className={`lock-task-btn ${isLocked ? "locked" : "unlocked"}`}>
        {isLocked ? 
            (
            <Lock size={20}/>
            ) : (
                <LockOpen size={20}/>
            )
        }
    </button>
  );
}

export default LockUnlockTask;
