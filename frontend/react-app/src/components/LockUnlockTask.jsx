import { useState } from 'react';
import { lockTask, unlockTask } from '../api/adminApi';

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
    <button onClick={handleLockUnlock}>
      {isLocked ? '🔒' : '🔓'}
    </button>
  );
}

export default LockUnlockTask;
