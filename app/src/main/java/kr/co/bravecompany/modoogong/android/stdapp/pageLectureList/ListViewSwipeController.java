package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.*;


class ListViewSwipeController extends Callback {


    public class MenuButton
    {
        public int bitmapResId;
        public Bitmap bitmapIcon;
        public String text;
        public int bgColor;
    }

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private ListViewSwipeControllerActions buttonsActions = null;

    private static final float buttonWidth = 300;// 300;
    private static final float swipeActiveLength= 100;


    private MenuButton rightButton;
    private MenuButton leftButton;
    private Context mContext;

    public ListViewSwipeController(Context context, ListViewSwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
        mContext = context;
    }


    public void SetRightButton(int color, int iconResId, String text)
    {
        rightButton = new MenuButton();
        rightButton.bitmapResId = iconResId;
        rightButton.bitmapIcon = BitmapFactory.decodeResource(mContext.getResources(), iconResId);
        rightButton.text = text;
        rightButton.bgColor = color;
    }

    public void SetLeftButton( int color, int iconResId, String text)
    {
        leftButton = new MenuButton();
        leftButton.bitmapResId = iconResId;
        leftButton.bitmapIcon = BitmapFactory.decodeResource(mContext.getResources(), iconResId);
        leftButton.text = text;
        leftButton.bgColor = color;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int swipeFlag = (rightButton !=null ? LEFT : 0) | (leftButton !=null ? RIGHT : 0);

        return makeMovementFlags(0, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;

                if (swipeBack) {

                    if ( rightButton!=null && dX < -swipeActiveLength) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if ( leftButton!=null && dX > swipeActiveLength) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    boolean mTouchDownInsideButton;

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    mTouchDownInsideButton = buttonInstance==null? false :  buttonInstance.contains(event.getX(), event.getY());

                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    ListViewSwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);

                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });

                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    if (buttonsActions != null && buttonInstance != null && mTouchDownInsideButton && buttonInstance.contains(event.getX(), event.getY()))
                    {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }

                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        buttonInstance = null;


        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        if(buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            RectF leftButtonRect = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(leftButton.bgColor);
            c.drawRoundRect(leftButtonRect, corners, corners, p);
            drawIcons(leftButton.bitmapIcon, c, leftButtonRect, p);
            if (leftButton.text != null)
                drawText(leftButton.text, c, leftButtonRect, p);

            buttonInstance = leftButtonRect;
        }

        if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            RectF rightButtonRect = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(rightButton.bgColor);
            c.drawRoundRect(rightButtonRect, corners, corners, p);
            drawIcons(rightButton.bitmapIcon, c, rightButtonRect, p);
            if (rightButton.text != null)
                drawText(rightButton.text, c, rightButtonRect, p);

            buttonInstance = rightButtonRect;
        }

//
//
//        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//            buttonInstance = rightButtonRect;
//        }
//        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//            buttonInstance = rightButtonRect;
//        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }
    private void drawIcons(Bitmap icon, Canvas c, RectF button, Paint p) {

        float x = button.centerX()-(icon.getWidth()/2);
        float y= button.centerY()-(icon.getHeight()/2);

        c.drawBitmap(icon, x,y, p);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}